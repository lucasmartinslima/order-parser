package com.picpay.OrderParser.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picpay.OrderParser.service.OrderParserService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderParserController {

    @Autowired
    OrderParserService orderParserService;

    @PostMapping("/parser")
    ResponseEntity<?> orderParser(@RequestParam("file") MultipartFile file){
        
        if (file.isEmpty()) {
            return new ResponseEntity<>("Por favor, selecione um arquivo", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderParserService.orderParser(file), HttpStatus.ACCEPTED);
    }
    
}
