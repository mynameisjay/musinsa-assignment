package com.musinsa.assignment.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class AssignmentDto2 {

    @JsonProperty(value="브랜드")
    private String brandName;

    @JsonProperty(value="카테고리")
    private List<ProductDto> products;

    @JsonProperty(value="총액")
    private String totalPrice;

}
