package com.ainais.boxingmarket.product.repository;

import com.ainais.boxingmarket.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}