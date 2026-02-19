package com.picpay.OrderParser.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.picpay.OrderParser.dto.response.OrderParserResponseDTO;
import com.picpay.OrderParser.model.Order;
import com.picpay.OrderParser.model.Product;

@Service
public class OrderParserService {

    public List<OrderParserResponseDTO> orderParser(MultipartFile file){
        
        Map<Integer, UserAccumulator> users = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String row;
            br.readLine();
            while ((row = br.readLine()) != null) {
               
                    Integer userId = Integer.parseInt(row.substring(0, 10).trim());
                    String userName = row.substring(10, 55).trim();
                    Integer orderId = Integer.parseInt(row.substring(55, 65).trim());
                    Integer productId = Integer.parseInt(row.substring(65, 75).trim());
                    String value = row.substring(75, 87).trim();

                    LocalDate formattedDate = LocalDate.parse(
                            row.substring(87, 95).trim(),
                            DateTimeFormatter.BASIC_ISO_DATE // yyyyMMdd
                    );

                    users.putIfAbsent(userId, new UserAccumulator(userId, userName));

                    UserAccumulator user = users.get(userId);
                    user.orders.putIfAbsent(orderId, new OrderAccumulator(orderId, formattedDate));

                    OrderAccumulator order = user.orders.get(orderId);
                    order.products.add(new Product(productId, value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<OrderParserResponseDTO> response = users.values().stream()
        .map(user -> new OrderParserResponseDTO(
                user.id,
                user.name,
                user.orders.values().stream()
                        .map(order -> new Order(
                                order.id,
                                order.products.stream()
                                        .map(Product::value)
                                        .reduce("0", (a, b) -> 
                                            new BigDecimal(a)
                                                .add(new BigDecimal(b))
                                                .toString()
                                        ),
                                order.date,
                                order.products
                        ))
                        .toList()
        ))
        .toList();


        return response;
    }

     private static class UserAccumulator {
        Integer id;
        String name;
        Map<Integer, OrderAccumulator> orders = new LinkedHashMap<>();

        UserAccumulator(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class OrderAccumulator {
        Integer id;
        LocalDate date;
        List<Product> products = new ArrayList<>();

        OrderAccumulator(Integer id, LocalDate date) {
            this.id = id;
            this.date = date;
        }
    }
}
