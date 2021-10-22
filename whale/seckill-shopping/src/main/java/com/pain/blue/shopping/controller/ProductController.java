package com.pain.blue.shopping.controller;

import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.shopping.domain.dto.ProductDestDTO;
import com.pain.blue.shopping.domain.dto.ProductDetailDTO;
import com.pain.blue.shopping.domain.pojo.ProductInfo;
import com.pain.blue.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/description/{id}")
    public RestResponse description(@PathVariable String id) {
        ProductInfo productInfo = productService.show(id);

        if (productInfo == null) {
            return RestResponse.error(404, "not found");
        }

        ProductDestDTO productDestDTO = CopyUtils.copy(productInfo, ProductDestDTO.class);
        productDestDTO.setTagText(productInfo.getTag() == 1 ? "普通商品" : "秒杀商品");
        return RestResponse.success(productDestDTO);
    }

    @GetMapping("/detail/{id}")
    public RestResponse detail(@PathVariable String id) {
        ProductInfo productInfo = productService.show(id);

        if (productInfo == null) {
            return RestResponse.error(404, "not found");
        }

        ProductDetailDTO productDetailDTO = CopyUtils.copy(productInfo, ProductDetailDTO.class);
        productDetailDTO.setTagText(productInfo.getTag() == 1 ? "普通商品" : "秒杀商品");
        productDetailDTO.setAvailable(false);
        return RestResponse.success(productDetailDTO);
    }
}
