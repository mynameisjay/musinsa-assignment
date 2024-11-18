package com.musinsa.assignment.domain.brand.repository;

import com.musinsa.assignment.domain.brand.entity.Brand;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface BrandCustomRepository {

    Optional<Brand> findOneLowestBundlePrice();

    void updateBundlePriceInBatch(Map<Long, Integer> bundlePrices);

}
