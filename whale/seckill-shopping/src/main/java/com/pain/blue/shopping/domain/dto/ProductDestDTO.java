package com.pain.blue.shopping.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDestDTO {
    private String productId;

    private String productName;

    private BigDecimal price;

    private String tagText;
}
