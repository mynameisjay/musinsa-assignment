package com.musinsa.assignment.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class AssignmentDto3 {

    @JsonProperty(value="카테고리")
    private String categoryName;

    @JsonProperty(value="최저가")
    private List<ProductDto> lowestPriceProducts;

    @JsonProperty(value="최고가")
    private List<ProductDto> highestPriceProducts;

}
