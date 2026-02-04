package com.ainais.boxingmarket.product.controller;

import com.ainais.boxingmarket.product.controller.dto.AddStockRequest;
import com.ainais.boxingmarket.product.controller.dto.ChangePriceRequest;
import com.ainais.boxingmarket.product.controller.dto.CreateProductRequest;
import com.ainais.boxingmarket.product.controller.dto.RemoveStockRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상품 등록 API 테스트")
    void createProduct() throws Exception {
        // given
        CreateProductRequest request = new CreateProductRequest("복싱 글러브", 50000, 100);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("상품 단건 조회 API 테스트")
    void getProduct() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        // when & then
        mockMvc.perform(get("/api/products/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("복싱 글러브"))
                .andExpect(jsonPath("$.price").value(50000))
                .andExpect(jsonPath("$.stockQuantity").value(100));
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 400 에러 반환")
    void getProduct_NotFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/products/{id}", 9999L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("상품이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("상품 가격 변경 API 테스트")
    void changePrice() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        ChangePriceRequest changePriceRequest = new ChangePriceRequest(60000);

        // when & then
        mockMvc.perform(patch("/api/products/{id}/price", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePriceRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        // 변경된 가격 확인
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(jsonPath("$.price").value(60000));
    }

    @Test
    @DisplayName("상품 가격을 음수로 변경 시 400 에러 반환")
    void changePrice_NegativePrice() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        ChangePriceRequest changePriceRequest = new ChangePriceRequest(-1000);

        // when & then
        mockMvc.perform(patch("/api/products/{id}/price", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePriceRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("재고 증가 API 테스트")
    void addStock() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        AddStockRequest addStockRequest = new AddStockRequest(50);

        // when & then
        mockMvc.perform(post("/api/products/{id}/stock/add", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStockRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        // 변경된 재고 확인
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(jsonPath("$.stockQuantity").value(150));
    }

    @Test
    @DisplayName("재고 증가 시 음수 수량 입력하면 400 에러 반환")
    void addStock_NegativeQuantity() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        AddStockRequest addStockRequest = new AddStockRequest(-10);

        // when & then
        mockMvc.perform(post("/api/products/{id}/stock/add", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("추가할 수량은 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("재고 감소 API 테스트")
    void removeStock() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        RemoveStockRequest removeStockRequest = new RemoveStockRequest(30);

        // when & then
        mockMvc.perform(post("/api/products/{id}/stock/remove", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeStockRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        // 변경된 재고 확인
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(jsonPath("$.stockQuantity").value(70));
    }

    @Test
    @DisplayName("재고보다 많은 수량 감소 시 400 에러 반환")
    void removeStock_InsufficientStock() throws Exception {
        // given - 상품 먼저 등록
        CreateProductRequest createRequest = new CreateProductRequest("복싱 글러브", 50000, 100);
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();
        Long productId = Long.parseLong(response);

        RemoveStockRequest removeStockRequest = new RemoveStockRequest(150);

        // when & then
        mockMvc.perform(post("/api/products/{id}/stock/remove", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("재고가 부족합니다."));
    }
}
