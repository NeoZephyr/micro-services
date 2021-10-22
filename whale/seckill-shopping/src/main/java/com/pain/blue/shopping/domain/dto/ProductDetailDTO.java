package com.pain.blue.shopping.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailDTO {

    private String productId;

    private String productName;

    private String pictureUrl;

    private BigDecimal price;

    private String tagText;

    private boolean available;
}
