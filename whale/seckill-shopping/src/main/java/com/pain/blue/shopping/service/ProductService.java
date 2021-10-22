package com.pain.blue.shopping.service;

import com.pain.blue.shopping.domain.pojo.ProductInfo;
import com.pain.blue.shopping.mapper.ProductInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductInfoMapper productInfoMapper;

    public ProductInfo show(String productId) {
        return productInfoMapper.selectByProductId(productId);
    }
}
