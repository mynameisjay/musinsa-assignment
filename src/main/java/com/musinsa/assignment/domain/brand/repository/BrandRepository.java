package com.musinsa.assignment.domain.brand.repository;

import com.musinsa.assignment.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, BrandCustomRepository {

    boolean existsByName(String name);

}
