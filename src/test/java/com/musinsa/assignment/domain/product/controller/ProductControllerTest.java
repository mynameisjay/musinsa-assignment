package com.musinsa.assignment.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assignment.domain.brand.service.BrandService;
import com.musinsa.assignment.domain.product.dto.ProductReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "scheduling.enable=false")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long EXIST_BRAND_ID = 1L;

    private static final Long NOT_EXIST_BRAND_ID = 100L;

    private static final Long EXIST_CATEGORY_ID = 1L;

    private static final Long NOT_EXIST_CATEGORY_ID = 100L;

    private static final Long EXIST_PRODUCT_ID = 1L;

    private static final Long NOT_EXIST_PRODUCT_ID = 8888L;

    private static final String DUMMY_NAME = "새상품";

    private static final Integer DUMMY_PRICE = 10001;


    @DisplayName("상품 추가 API")
    @Test
    void create() throws Exception {
        // [fail case 1] given
        ProductReqDto reqDto = ProductReqDto.builder()
                .brandId(NOT_EXIST_BRAND_ID)
                .categoryId(EXIST_CATEGORY_ID)
                .name(DUMMY_NAME)
                .price(DUMMY_PRICE)
                .build();

        // [fail case 1] when & then
        mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                    .value("존재하지 않는 brandId 입니다."));


        // [fail case 2] given
        reqDto.setBrandId(EXIST_BRAND_ID);
        reqDto.setCategoryId(NOT_EXIST_CATEGORY_ID);

        // [fail case 2] when & then
        mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                    .value("존재하지 않는 categoryId 입니다."));

        // [success case] given
        reqDto.setBrandId(EXIST_BRAND_ID);
        reqDto.setCategoryId(EXIST_CATEGORY_ID);

        // [success case] when & then
        mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("상품 업데이트 API")
    @Test
    void update() throws Exception {
        // [fail case] given
        ProductReqDto reqDto = ProductReqDto.builder()
                .brandId(EXIST_BRAND_ID)
                .categoryId(EXIST_CATEGORY_ID)
                .name(DUMMY_NAME)
                .price(DUMMY_PRICE)
                .build();
        Long productId = NOT_EXIST_PRODUCT_ID;

        // [fail case] when & then
        mockMvc.perform(put("/products/" + productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                    .value("존재하지 않는 id 입니다."));

        // [success case] given
        productId = EXIST_PRODUCT_ID;

        // [success case] when & then
        mockMvc.perform(put("/products/" + productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("상품 삭제 API")
    @Test
    void delete() throws Exception {
        // [fail case] given
        Long productId = NOT_EXIST_PRODUCT_ID;

        // [fail case] when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/products/" + productId))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                    .value("존재하지 않는 id 입니다."));

        // [success case] given
        productId = EXIST_PRODUCT_ID;

        // [success case] when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/products/" + productId))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("카테고리별 최저가격 브랜드, 상품가격, 총액 조회 API")
    @Test
    void getLowestPriceByCategory() throws Exception {
        // initial data given and then
        mockMvc.perform(get(
            "/products/aggregation/category/lowest-price"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.총액")
                    .value("34,100"));
    }


    @DisplayName("단일 브랜드로 모든 카테고리 묶음구매시 최저가 브랜드 조회 API")
    @Test
    void getBundleLowestPriceByBrand() throws Exception {
        // initial data given (집계)
        brandService.updateBundlePriceInBatch();

        // when & then
        mockMvc.perform(get(
            "/products/aggregation/brand/lowest-bundle-price"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.브랜드")
                    .value("D"))
            .andExpect(jsonPath("$.data.총액")
                    .value("36,100"));
    }


    @DisplayName("카테고리명으로 최저, 최고가격 브랜드와 상품가격 조회 API")
    @Test
    void getLowestHighestPriceByCategoryName() throws Exception {
        // [fail case] given
        String categoryName = "";

        // [fail case] when & then
        mockMvc.perform(get(
            "/products/aggregation/category/lowest-highest-price")
                        .queryParam("categoryName", categoryName))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                    .value("조회 가능한 데이터가 없습니다."));

        // [success case] given
        categoryName = "상의";

        // [success case] when & then
        mockMvc.perform(get(
            "/products/aggregation/category/lowest-highest-price")
                        .queryParam("categoryName", categoryName))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.카테고리")
                    .value(categoryName))
            .andExpect(jsonPath("$.data.최저가[0].브랜드")
                    .value("C"))
            .andExpect(jsonPath("$.data.최저가[0].가격")
                    .value("10,000"))
            .andExpect(jsonPath("$.data.최고가[0].브랜드")
                    .value("I"))
            .andExpect(jsonPath("$.data.최고가[0].가격")
                    .value("11,400"));
    }

}