package com.picpay.OrderParser.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Order(
    @JsonProperty("order_id")
    Integer id,
    String total,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,
    List<Product> products
) {
}