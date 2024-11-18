package com.musinsa.assignment.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LowestHighestPriceDto {

    private Long categoryId;

    private Integer lowestPrice;

    private Integer highestPrice;


    @QueryProjection
    public LowestHighestPriceDto(Long categoryId, Integer lowestPrice, Integer highestPrice) {
        this.categoryId = categoryId;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
    }

    public List<PriceDto> toPriceDtos() {
        List<PriceDto> toPriceDtos = new ArrayList<>();

        toPriceDtos.add(PriceDto.builder()
                .categoryId(this.categoryId)
                .price(this.lowestPrice)
                .build());

        toPriceDtos.add(PriceDto.builder()
                .categoryId(this.categoryId)
                .price(this.highestPrice)
                .build());

        return toPriceDtos;
    }

}
