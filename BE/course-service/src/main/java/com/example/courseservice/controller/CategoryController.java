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

import static com.example.courseservice.mapper.CategoryMapper.mapToCategoryDto;

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
        return ResponseEntity.ok(mapToCategoryDto(saved));
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

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<Category> categories = categoryService.getAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(mapToCategoryDto(category));
        }
        return ResponseEntity.ok(categoryDtos);
    }
}
