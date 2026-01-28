package com.ainais.boxingmarket.product.controller.dto;

import com.ainais.boxingmarket.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int stockQuantity
) {
    // 이 static 메서드가 없으면 Controller에서 에러가 납니다.
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}