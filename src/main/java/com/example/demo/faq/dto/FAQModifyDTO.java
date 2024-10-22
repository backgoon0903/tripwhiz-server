package com.example.demo.faq.dto;

import com.example.demo.common.domain.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQModifyDTO {

    private Long fno;

    private CategoryEntity category;

    private String question;

    private String answer;

}