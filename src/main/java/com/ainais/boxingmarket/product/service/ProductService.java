package com.ainais.boxingmarket.product.service;

import com.ainais.boxingmarket.product.domain.Product;
import com.ainais.boxingmarket.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Transactional
    public Long createProduct(String name, int price, int stock) {
        Product product = new Product(name, price, stock);
        return productRepository.save(product).getId();
    }

    @Transactional
    public void changePrice(Long productId, int newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        // 핵심: 서비스가 값을 바꾸지 않고, 도메인에게 명령을 내림
        product.changePrice(newPrice);
        // JPA Dirty Checking에 의해 자동 저장됨
    }
}