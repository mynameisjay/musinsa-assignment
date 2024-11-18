package com.musinsa.assignment.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    @JsonProperty(value="카테고리")
    private String categoryName;

    @JsonProperty(value="브랜드")
    private String brandName;

    @JsonProperty(value="가격")
    private String price;

}
