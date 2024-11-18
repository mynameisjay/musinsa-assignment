package com.musinsa.assignment.domain.product.controller;

import com.musinsa.assignment.common.dto.ApiResponse;
import com.musinsa.assignment.domain.product.dto.AssignmentDto1;
import com.musinsa.assignment.domain.product.dto.AssignmentDto2;
import com.musinsa.assignment.domain.product.dto.AssignmentDto3;
import com.musinsa.assignment.domain.product.dto.ProductReqDto;
import com.musinsa.assignment.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 API")
@Validated
@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductController {

    private final ProductService productService;


    @Operation(summary = "[구현 4번] 상품 추가 API")
    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid ProductReqDto reqDto) {
        productService.create(
                reqDto.getBrandId(),
                reqDto.getCategoryId(),
                reqDto.getName(),
                reqDto.getPrice()
        );
        return ApiResponse.success();
    }


    @Operation(summary = "[구현 4번] 상품 업데이트 API")
    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable(name = "id") Long id,
                                 @RequestBody @Valid ProductReqDto reqDto) {
        productService.update(
                id,
                reqDto.getBrandId(),
                reqDto.getCategoryId(),
                reqDto.getName(),
                reqDto.getPrice()
        );
        return ApiResponse.success();
    }


    @Operation(summary = "[구현 4번] 상품 삭제 (Soft Delete) API")
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable(name = "id") Long id) {
        productService.delete(id);
        return ApiResponse.success();
    }


    /**
     * [이하 공통] 과제 요구사항 문서의 샘플 응답값을 참고하여, 모든 응답 Dto 의 data 항목 이하는 '한글 필드명'으로 통일하였습니다.
     * */

    @Operation(summary = "[구현 1번] 요구사항 API")
    @GetMapping("/aggregation/category/lowest-price")
    public ApiResponse<AssignmentDto1> getLowestPriceByCategory() {
        return ApiResponse.success(productService.getLowestPriceByCategory());
    }


    @Operation(summary = "[구현 2번] 요구사항 API")
    @GetMapping("/aggregation/brand/lowest-bundle-price")
    public ApiResponse<AssignmentDto2> getBundleLowestPriceByBrand() {
        return ApiResponse.success(productService.getBundleLowestPriceByBrand());
    }


    @Operation(summary = "[구현 3번] 요구사항 API")
    @GetMapping("/aggregation/category/lowest-highest-price")
    public ApiResponse<AssignmentDto3> getLowestHighestPriceByCategoryName(
                @RequestParam(name = "categoryName") String categoryName) {
        return ApiResponse.success(
                productService.getLowestHighestPriceByCategoryName(categoryName)
        );
    }

}
