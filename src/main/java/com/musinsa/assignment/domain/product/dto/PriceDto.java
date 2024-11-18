package com.musinsa.assignment.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceDto {

    private Long brandId;

    private Long categoryId;

    private Integer price;


    @Builder
    @QueryProjection
    public PriceDto(Long categoryId, Integer price) {
        this.categoryId = categoryId;
        this.price = price;
    }

    @QueryProjection
    public PriceDto(Long brandId, Long categoryId, Integer price) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.price = price;
    }

}
