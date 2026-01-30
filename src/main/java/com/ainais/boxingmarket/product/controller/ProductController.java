package com.ainais.boxingmarket.product.controller;

import com.ainais.boxingmarket.product.controller.dto.AddStockRequest;
import com.ainais.boxingmarket.product.controller.dto.ChangePriceRequest;
import com.ainais.boxingmarket.product.controller.dto.CreateProductRequest;
import com.ainais.boxingmarket.product.controller.dto.ProductResponse;
import com.ainais.boxingmarket.product.controller.dto.RemoveStockRequest;
import com.ainais.boxingmarket.product.domain.Product;
import com.ainais.boxingmarket.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "상품(Product) API", description = "상품 등록, 조회 및 가격 수정 기능")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품명, 가격, 재고를 입력받아 새로운 상품을 등록합니다.")
    public Long createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(
                request.name(),
                request.price(),
                request.stockQuantity()
        );
    }

    @PatchMapping("/{id}/price")
    @Operation(summary = "상품 가격 변경", description = "단순 수정이 아닌, 비즈니스 로직에 따른 가격 변경 행위를 수행합니다.")
    public void changePrice(@PathVariable Long id, @RequestBody ChangePriceRequest request) {
        productService.changePrice(id, request.newPrice());
    }

    @PostMapping("/{id}/stock/add")
    @Operation(summary = "재고 증가 (상품 입고)", description = "상품의 재고를 주어진 수량만큼 증가시킵니다.")
    public void addStock(@PathVariable Long id, @RequestBody AddStockRequest request) {
        productService.addStock(id, request.quantity());
    }

    @PostMapping("/{id}/stock/remove")
    @Operation(summary = "재고 감소 (상품 주문)", description = "상품의 재고를 주어진 수량만큼 감소시킵니다. 재고가 부족하면 에러가 발생합니다.")
    public void removeStock(@PathVariable Long id, @RequestBody RemoveStockRequest request) {
        productService.removeStock(id, request.quantity());
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 단건 조회", description = "상품 ID로 상세 정보를 조회합니다.")
    public ProductResponse getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ProductResponse.from(product);
    }
}