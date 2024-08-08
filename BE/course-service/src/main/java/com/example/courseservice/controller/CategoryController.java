package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.Category;
import com.example.courseservice.entity.Course;
import com.example.courseservice.mapper.CourseMapper;
import com.example.courseservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/category")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping("create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        Category saved = categoryService.save(category);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(saved.getId());
        categoryDto.setName(saved.getName());
        return ResponseEntity.ok(categoryDto);
    }

    @PostMapping("addCourse")
    public ResponseEntity<BaseResponse> addCourseToCategory(@Valid @RequestBody CourseCategoryDto dto) {
        categoryService.addCourse(dto.getCategoryId(), dto.getCourseId());
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @PostMapping("removeCourse")
    public ResponseEntity<?> removeCourseFromCategory(@Valid @RequestBody CourseCategoryDto dto) {
        categoryService.removeCourse(dto.getCategoryId(), dto.getCourseId());
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(mapToCategoryDto(category));
    }

    @GetMapping("{categoryId}/courses")
    public ResponseEntity<List<CourseDto>> listCoursesPerCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course c : category.getCourses()) {
            courseDtos.add(CourseMapper.mappedToCourseDto(c));
        }
        return ResponseEntity.ok(courseDtos);
    }

    @PostMapping("{categoryId}/update")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto dto) {
        Category category = categoryService.updateCategory(categoryId, dto);
        return ResponseEntity.ok(mapToCategoryDto(category));
    }

    @DeleteMapping("{categoryId}/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        categoryService.delete(category);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("search")
    public ResponseEntity<List<CategorySearchResponse>> search(@RequestParam("keyword") String keyword) {
        List<CategorySearchResponse> responses = categoryService.search(keyword);
        return ResponseEntity.ok(responses);
    }

    private CategoryDto mapToCategoryDto(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
}
