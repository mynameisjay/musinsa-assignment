package com.musinsa.assignment.domain.brand.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandReqDto {

    @NotEmpty(message = "name : 필수 입력 값입니다.")
    private String name;

}
