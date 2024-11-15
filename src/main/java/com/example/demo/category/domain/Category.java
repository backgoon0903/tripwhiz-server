package com.example.demo.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @Builder.Default
    private ParentCategory category = ParentCategory.All;

    private boolean delFlag;

    @Builder.Default
    private ThemeCategory themeCategory = ThemeCategory.RELAXATION;

}
