package com.musinsa.assignment.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class AssignmentDto1 {

    @JsonProperty(value="카테고리별_최저가")
    private List<ProductDto> products;

    @JsonProperty(value="총액")
    private String totalPrice;

}
