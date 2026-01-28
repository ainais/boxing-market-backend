package com.ainais.boxingmarket.product.controller.dto;

public record CreateProductRequest(
        String name,
        int price,
        int stockQuantity
) {}