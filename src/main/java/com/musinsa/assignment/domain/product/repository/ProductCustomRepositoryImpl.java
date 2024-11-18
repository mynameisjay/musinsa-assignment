package com.musinsa.assignment.domain.product.repository;

import com.musinsa.assignment.domain.brand.entity.QBrand;
import com.musinsa.assignment.domain.category.entity.QCategory;
import com.musinsa.assignment.domain.product.dto.LowestHighestPriceDto;
import com.musinsa.assignment.domain.product.dto.PriceDto;
import com.musinsa.assignment.domain.product.dto.QLowestHighestPriceDto;
import com.musinsa.assignment.domain.product.dto.QPriceDto;
import com.musinsa.assignment.domain.product.entity.Product;
import com.musinsa.assignment.domain.product.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;

    private final QBrand brand = QBrand.brand;

    private final QCategory category = QCategory.category;


    @Override
    public List<PriceDto> findAllLowestPrice() {
        return queryFactory
                .select(new QPriceDto(product.categoryId, product.price.min()))
                .from(product)
                .join(product.brand, brand)
                .where(isAvailable())
                .groupBy(product.categoryId)
                .fetch();
    }

    @Override
    public List<PriceDto> findAllLowestPriceByBrandIds(List<Long> brandIds) {
        return queryFactory
                .select(new QPriceDto(product.brandId, product.categoryId,
                        product.price.min()))
                .from(product)
                .join(product.brand, brand)
                .where(product.brandId.in(brandIds).and(isAvailable()))
                .groupBy(product.brandId, product.categoryId)
                .fetch();
    }

    @Override
    public LowestHighestPriceDto findLowestHighestPriceByCategoryName(String categoryName) {
        return queryFactory
                .select(new QLowestHighestPriceDto(product.categoryId,
                        product.price.min(), product.price.max()))
                .from(product)
                .join(product.brand, brand)
                .join(product.category, category)
                .where(category.name.eq(categoryName).and(isAvailable()))
                .groupBy(product.categoryId)
                .fetchOne();
    }

    @Override
    public List<Product> findAllByFieldsIn(List<PriceDto> priceDtos) {
        // 필드조합 조회 (multi column in절 미지원으로 'or' 사용)
        BooleanBuilder allConditions = new BooleanBuilder();

        priceDtos.forEach(lp -> {
            BooleanBuilder condition = new BooleanBuilder();
            if(lp.getBrandId() != null)
                condition.and(product.brandId.eq(lp.getBrandId()));
            if(lp.getCategoryId() != null)
                condition.and(product.categoryId.eq(lp.getCategoryId()));
            if(lp.getPrice() != null)
                condition.and(product.price.eq(lp.getPrice()));
            allConditions.or(condition);
        });
        return queryFactory
                .select(product)
                .from(product)
                .join(product.brand, brand).fetchJoin()
                .join(product.category, category).fetchJoin()
                .where(allConditions.and(isAvailable()))
                .orderBy(product.categoryId.asc(), product.id.desc())
                .fetch();
    }

    private BooleanExpression isAvailable() {
        return product.isDeleted.eq(false)
                .and(brand.isDeleted.eq(false));
    }


}
