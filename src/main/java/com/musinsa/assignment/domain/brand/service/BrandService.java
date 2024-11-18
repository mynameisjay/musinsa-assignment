package com.musinsa.assignment.domain.brand.service;

import com.musinsa.assignment.common.exception.ApiException;
import com.musinsa.assignment.common.exception.ErrorType;
import com.musinsa.assignment.domain.brand.entity.Brand;
import com.musinsa.assignment.domain.brand.repository.BrandRepository;
import com.musinsa.assignment.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    private final ProductService productService;


    @Transactional
    public void create(String name) {
        this.validDuplicatedName(name);
        Brand brand = Brand.builder()
                .name(name)
                .build();
        brandRepository.save(brand);
    }


    @Transactional
    public void update(Long id, String name) {
        Brand brand = this.getById(id);
        this.validDuplicatedName(name);
        brand.update(name);
        brandRepository.save(brand);
    }


    @Transactional
    public void delete(Long id) {
        Brand brand = this.getById(id);
        brand.softDelete();
        brandRepository.save(brand);
    }


    @Transactional(readOnly = true)
    public Brand getById(Long id) {
        return brandRepository.findById(id).orElseThrow(
            () -> new ApiException(ErrorType.NOT_FOUND_ERROR, "존재하지 않는 id 입니다."));
    }


    @Transactional(readOnly = true)
    private void validDuplicatedName(String name) {
        if(brandRepository.existsByName(name))
            throw new ApiException(ErrorType.CONFLICT_ERROR, "이미 존재하는 name 입니다.");
    }


    @Transactional(readOnly = true)
    public void updateBundlePriceInBatch() {
        int chunkSize = 100;
        int nextPage = 0;
        boolean isLast;
        Pageable pageable = Pageable.ofSize(chunkSize);

        do {
            Page<Brand> brands = brandRepository.findAll(pageable.withPage(nextPage));
            List<Long> brandIds = brands.getContent().stream()
                    .map(Brand::getId)
                    .toList();

            // 단일 브랜드로 모든 카테고리 상품 1개씩 묶음구매시의 합산가격 빠른 조회를 위해 미리 집계 후 업데이트
            Map<Long, Integer> bundlePrices = productService.getBundlePriceByBrandIds(brandIds);
            brandRepository.updateBundlePriceInBatch(bundlePrices);

            nextPage += 1;
            isLast = brands.isLast();

        } while (!isLast);
    }

}
