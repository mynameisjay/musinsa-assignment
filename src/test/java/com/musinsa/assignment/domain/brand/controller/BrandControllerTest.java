package com.musinsa.assignment.domain.brand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assignment.domain.brand.dto.BrandReqDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "scheduling.enable=false")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long EXIST_BRAND_ID = 1L;

    private static final Long NOT_EXIST_BRAND_ID = 100L;

    private static final String EXIST_BRAND_NAME = "A";

    private static final String NOT_EXIST_BRAND_NAME = "새브랜드";


    @DisplayName("브랜드 추가 API")
    @Test
    void create() throws Exception {
        // [fail case 1] given
        BrandReqDto reqDto = BrandReqDto.builder()
                .name(EXIST_BRAND_NAME)
                .build();

        // [fail case 1] when & then
        mockMvc.perform(post("/brands")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message")
                    .value("이미 존재하는 name 입니다."));

        // [success case] given
        reqDto.setName(NOT_EXIST_BRAND_NAME);

        // [success case] when & then
        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("브랜드 업데이트 API")
    @Test
    void update() throws Exception {
        // [fail case 1] given
        BrandReqDto reqDto = BrandReqDto.builder()
                .name(NOT_EXIST_BRAND_NAME)
                .build();
        Long brandId = NOT_EXIST_BRAND_ID;

        // [fail case 1] when & then
        mockMvc.perform(put("/brands/" + brandId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                        .value("존재하지 않는 id 입니다."));

        // [success case] given
        brandId = EXIST_BRAND_ID;

        // [success case] when & then
        mockMvc.perform(put("/brands/" + brandId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("브랜드 삭제 API")
    @Test
    void delete() throws Exception {
        // [fail case] given
        Long brandId = NOT_EXIST_BRAND_ID;

        // [fail case] when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/brands/" + brandId))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message")
                        .value("존재하지 않는 id 입니다."));

        // [success case] given
        brandId = EXIST_BRAND_ID;

        // [success case] when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/brands/" + brandId))
                .andDo(print())
                .andExpect(status().isOk());
    }

}