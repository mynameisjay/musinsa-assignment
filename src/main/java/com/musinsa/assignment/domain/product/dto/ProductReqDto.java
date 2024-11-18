package com.musinsa.assignment.domain.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReqDto {

    @NotNull(message = "brandId : 필수 입력 값입니다.")
    private Long brandId;

    @NotNull(message = "categoryId : 필수 입력 값입니다.")
    private Long categoryId;

    @NotEmpty(message = "name : 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "price : 필수 입력 값입니다.")
    private Integer price;

}
