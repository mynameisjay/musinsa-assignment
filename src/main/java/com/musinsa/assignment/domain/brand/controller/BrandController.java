package com.musinsa.assignment.domain.brand.controller;

import com.musinsa.assignment.common.dto.ApiResponse;
import com.musinsa.assignment.domain.brand.dto.BrandReqDto;
import com.musinsa.assignment.domain.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "브랜드 API")
@Validated
@RequiredArgsConstructor
@RequestMapping("/brands")
@RestController
public class BrandController {

    private final BrandService brandService;


    @Operation(summary = "[구현 4번] 브랜드 추가 API")
    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid BrandReqDto reqDto) {
        brandService.create(reqDto.getName());
        return ApiResponse.success();
    }


    @Operation(summary = "[구현 4번] 브랜드 업데이트 API")
    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable(name = "id") Long id,
                                 @RequestBody @Valid BrandReqDto reqDto) {
        brandService.update(id, reqDto.getName());
        return ApiResponse.success();
    }


    @Operation(summary = "[구현 4번] 브랜드 삭제 (Soft Delete) API")
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable(name = "id") Long id) {
        brandService.delete(id);
        return ApiResponse.success();
    }


    @Operation(summary = "집계용 API", hidden = true)
    @PostMapping("/batch")
    public void updateBundlePriceInBatch() {
        brandService.updateBundlePriceInBatch();
    }

}
