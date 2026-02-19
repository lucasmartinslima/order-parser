package com.picpay.OrderParser.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.OrderParser.model.Order;

public record OrderParserResponseDTO(
    @JsonProperty("user_id")
    Integer id,
    String name,
    List<Order> orders
) {
    
}
