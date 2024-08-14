package com.example.courseservice.mapper;

import com.example.courseservice.dto.CategoryDto;
import com.example.courseservice.entity.Category;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
}