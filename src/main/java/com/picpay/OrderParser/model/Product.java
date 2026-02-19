package com.picpay.OrderParser.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Product(
    @JsonProperty("product_id")
    Integer id,
    String value
) {
    
}
