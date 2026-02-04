package com.ainais.boxingmarket.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자 (외부 호출 방지)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    // 생성자: 객체가 생성될 때부터 유효한 상태임을 보장
    public Product(String name, int price, int stockQuantity) {
        verifyPrice(price);
        verifyStock(stockQuantity);
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // --- 비즈니스 로직 (Setter 대신 사용) ---

    // 가격 변경 로직
    public void changePrice(int newPrice) {
        verifyPrice(newPrice);
        this.price = newPrice;
    }

    // 재고 증가
    public void addStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("추가할 수량은 0 이상이어야 합니다.");
        }
        this.stockQuantity += quantity;
    }

    // 재고 감소 (주문 시 사용) -> 비즈니스 규칙: 재고 부족 시 예외 발생
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다."); // 커스텀 예외로 교체 권장
        }
        this.stockQuantity = restStock;
    }

    // --- 검증 로직 ---
    private void verifyPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    private void verifyStock(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("재고는 0개 이상이어야 합니다.");
        }
    }
}