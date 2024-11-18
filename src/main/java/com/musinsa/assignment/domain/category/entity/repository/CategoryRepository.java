package com.musinsa.assignment.domain.category.entity.repository;

import com.musinsa.assignment.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
