package com.musinsa.assignment.domain.brand.repository;

import com.musinsa.assignment.domain.brand.entity.Brand;
import com.musinsa.assignment.domain.brand.entity.QBrand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BrandCustomRepositoryImpl implements BrandCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBrand brand = QBrand.brand;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Optional<Brand> findOneLowestBundlePrice() {
        return Optional.ofNullable(queryFactory
                .select(brand)
                .from(brand)
                .where(brand.isDeleted.eq(false)
                    .and(brand.bundlePrice.gt(0))
                    .and(brand.aggregatedAt.isNotNull()))
                .orderBy(brand.bundlePrice.asc())
                .fetchFirst());
    }


    @Override
    public void updateBundlePriceInBatch(Map<Long, Integer> bundlePrices) {
        // bulk update
        String sql = "UPDATE brand SET bundle_price=:bundlePrice, " +
                    "aggregated_at=:aggregatedAt WHERE id=:id";

        List<MapSqlParameterSource> params = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        bundlePrices.forEach((key, value) -> {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", key);
            source.addValue("bundlePrice", value);
            source.addValue("aggregatedAt", now);
            params.add(source);
        });
        namedParameterJdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

}
