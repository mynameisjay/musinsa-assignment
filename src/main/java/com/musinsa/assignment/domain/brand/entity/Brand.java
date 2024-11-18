package com.musinsa.assignment.domain.brand.entity;

import com.musinsa.assignment.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand")
@Entity
public class Brand extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private Long bundlePrice;

    @Column
    private Boolean isDeleted;

    @Column
    private LocalDateTime aggregatedAt;

    @Builder
    public Brand(String name) {
        this.name = name;
        this.bundlePrice = 0L;
        this.isDeleted = false;
        this.aggregatedAt = null;
    }

    public void update(String name) {
        this.name = name;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

}
