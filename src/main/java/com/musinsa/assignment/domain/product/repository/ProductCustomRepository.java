package com.musinsa.assignment.domain.product.repository;

import com.musinsa.assignment.domain.product.dto.LowestHighestPriceDto;
import com.musinsa.assignment.domain.product.dto.PriceDto;
import com.musinsa.assignment.domain.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCustomRepository {

    List<PriceDto> findAllLowestPrice();

    List<PriceDto> findAllLowestPriceByBrandIds(List<Long> brandIds);

    LowestHighestPriceDto findLowestHighestPriceByCategoryName(String categoryName);

    List<Product> findAllByFieldsIn(List<PriceDto> dtoList);

}
