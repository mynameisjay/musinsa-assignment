package com.musinsa.assignment.domain.product.entity;

import com.musinsa.assignment.common.entity.BaseTime;
import com.musinsa.assignment.domain.brand.entity.Brand;
import com.musinsa.assignment.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long brandId;

    @Column
    private Long categoryId;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", insertable = false, updatable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private Category category;


    @Builder
    public Product(Long brandId, Long categoryId, String name, Integer price) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.isDeleted = false;
    }

    public void update(Long brandId, Long categoryId, String name, Integer price) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

}
