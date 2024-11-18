package com.musinsa.assignment.domain.product.service;

import com.musinsa.assignment.common.exception.ApiException;
import com.musinsa.assignment.common.exception.ErrorType;
import com.musinsa.assignment.domain.brand.entity.Brand;
import com.musinsa.assignment.domain.brand.repository.BrandRepository;
import com.musinsa.assignment.domain.category.entity.repository.CategoryRepository;
import com.musinsa.assignment.domain.product.dto.*;
import com.musinsa.assignment.domain.product.entity.Product;
import com.musinsa.assignment.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.musinsa.assignment.common.util.FormatterUtil.toPriceFormat;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;


    @Transactional
    public void create(Long brandId, Long categoryId, String name, Integer price) {
        this.validBrandIdAndCategoryId(brandId, categoryId);
        Product product = Product.builder()
                .brandId(brandId)
                .categoryId(categoryId)
                .name(name)
                .price(price)
                .build();
        productRepository.save(product);
    }


    @Transactional
    public void update(Long id, Long brandId, Long categoryId, String name, Integer price) {
        Product product = this.getById(id);
        this.validBrandIdAndCategoryId(brandId, categoryId);
        product.update(brandId, categoryId, name, price);
        productRepository.save(product);
    }


    @Transactional
    public void delete(Long id) {
        Product product = this.getById(id);
        product.softDelete();
        productRepository.save(product);
    }


    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorType.NOT_FOUND_ERROR, "존재하지 않는 id 입니다."));
    }


    @Transactional(readOnly = true)
    private void validBrandIdAndCategoryId(Long brandId, Long categoryId) {
        brandRepository.findById(brandId).orElseThrow(() ->
                new ApiException(ErrorType.NOT_FOUND_ERROR, "존재하지 않는 brandId 입니다."));

        categoryRepository.findById(categoryId).orElseThrow(() ->
                new ApiException(ErrorType.NOT_FOUND_ERROR, "존재하지 않는 categoryId 입니다."));
    }


    @Transactional(readOnly = true)
    public AssignmentDto1 getLowestPriceByCategory() {
        // 전체 카테고리별 최저가격 조회 (group by)
        List<PriceDto> lowestPriceByCategory = productRepository.findAllLowestPrice();

        // 조회된 카테고리+최저가격 조합으로 상품조회
        List<Product> products = productRepository.findAllByFieldsIn(lowestPriceByCategory);

        // 같은 카테고리내 동일가격 최저가 상품이 2개이상 존재할 수 있기에, 요구사항(1개씩만 출력)에 따라 중복제거
        List<Product> result = this.removeDuplicatesByCategoryIdAndPrice(products);

        return AssignmentDto1.builder()
                .products(
                    result.stream()
                    .map(p -> ProductDto.builder()
                        .categoryName(p.getCategory().getName())
                        .brandName(p.getBrand().getName())
                        .price(toPriceFormat(p.getPrice()))
                        .build())
                    .toList()
                )
                .totalPrice(toPriceFormat(this.sumPrice(result)))
                .build();
    }


    @Transactional(readOnly = true)
    public AssignmentDto2 getBundleLowestPriceByBrand() {
        // 단일 브랜드로 전체 카테고리 묶음구매시 최저가 브랜드
        Brand brand = brandRepository.findOneLowestBundlePrice().orElseThrow(
                () -> new ApiException(ErrorType.NOT_FOUND_ERROR, "조회 가능한 브랜드가 존재하지 않습니다."));

        // 브랜드 + 전체 카테고리 최저가격 조회 (group by)
        List<PriceDto> lowestPriceByBrandAndCategory =
                productRepository.findAllLowestPriceByBrandIds(List.of(brand.getId()));

        // 조회된 브랜드+카테고리+최저가격 조합으로 상품조회
        List<Product> products = productRepository.findAllByFieldsIn(lowestPriceByBrandAndCategory);

        // 같은 카테고리내 동일가격 최저가 상품이 2개이상 존재할 수 있기에, 요구사항(1개씩만 출력)에 따라 중복제거
        List<Product> result = this.removeDuplicatesByCategoryIdAndPrice(products);

        return AssignmentDto2.builder()
                .brandName(brand.getName())
                .products(
                    result.stream()
                    .map(p -> ProductDto.builder()
                        .categoryName(p.getCategory().getName())
                        .price(toPriceFormat(p.getPrice()))
                        .build())
                    .toList()
                )
                .totalPrice(toPriceFormat(this.sumPrice(result)))
                .build();
    }


    @Transactional(readOnly = true)
    public Map<Long, Integer> getBundlePriceByBrandIds(List<Long> brandIds) {
        // 브랜드 + 전체 카테고리 최저가격 조회 (group by)
        List<PriceDto> lowestPriceByBrandAndCategory =
                productRepository.findAllLowestPriceByBrandIds(brandIds);

        Map<Long, Integer> result = new HashMap<>();
        Map<Long, Integer> categoryCount = new HashMap<>();

        // 조회된 데이터 brandId 기준 가격합산
        lowestPriceByBrandAndCategory.forEach(lp -> {
            result.put(lp.getBrandId(), result.getOrDefault(lp.getBrandId(), 0) + lp.getPrice());
            categoryCount.put(lp.getBrandId(), categoryCount.getOrDefault(lp.getBrandId(), 0) + 1);
        });

        // 총 8개 모든 카테고리에 상품이 존재하는 브랜드만 집계대상
        categoryCount.forEach((key, value) -> {
            if(value < 8)
                result.remove(key);
        });
        return result;
    }


    @Transactional(readOnly = true)
    public AssignmentDto3 getLowestHighestPriceByCategoryName(String categoryName) {
        // 카테고리명으로 최저가격, 최고가격 조회 (group by)
        LowestHighestPriceDto lowestHighestPrice =
                productRepository.findLowestHighestPriceByCategoryName(categoryName);

        if(lowestHighestPrice == null)
            throw new ApiException(ErrorType.NOT_FOUND_ERROR, "조회 가능한 데이터가 없습니다.");

        // 조회된 카테고리 + 가격 조합으로 상품조회
        List<Product> products = productRepository.findAllByFieldsIn(lowestHighestPrice.toPriceDtos());

        return AssignmentDto3.builder()
                .categoryName(categoryName)
                .lowestPriceProducts(products.stream()
                    .filter(p -> p.getPrice().equals(lowestHighestPrice.getLowestPrice()))
                    .map(p -> ProductDto.builder()
                        .brandName(p.getBrand().getName())
                        .price(toPriceFormat(p.getPrice()))
                        .build())
                    .toList())
                .highestPriceProducts(products.stream()
                    .filter(p -> p.getPrice().equals(lowestHighestPrice.getHighestPrice()))
                    .map(p -> ProductDto.builder()
                            .brandName(p.getBrand().getName())
                            .price(toPriceFormat(p.getPrice()))
                            .build())
                    .toList())
                .build();
    }


    private List<Product> removeDuplicatesByCategoryIdAndPrice(List<Product> products) {
        List<Product> result = new ArrayList<>();
        products.forEach(p -> {
            boolean isExists = result.stream()
                    .anyMatch(r -> r.getCategoryId().equals(p.getCategoryId())
                        && r.getPrice().equals(p.getPrice()));
            if(!isExists)
                result.add(p);
        });
        return result;
    }


    private Integer sumPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

}
