package com.example.courseservice.service;

import com.example.courseservice.dto.CategoryDto;
import com.example.courseservice.dto.CategorySearchResponse;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.entity.Category;
import com.example.courseservice.entity.Course;
import com.example.courseservice.exception.CourseAlreadyAddedException;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.mapper.CourseMapper;
import com.example.courseservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;
    private CourseService courseService;

    public Category save(Category c) {
        return categoryRepository.save(c);
    }

    public void addCourse(Long categoryId, Long courseId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) throw new SearchNotFoundException("Category not found");
        Category category = optionalCategory.get();

        Course course = courseService.getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");

        if (category.getCourses().contains(course)) throw new CourseAlreadyAddedException("Course already added");
        category.getCourses().add(course);
        course.getCategories().add(category);
        save(category);
//        courseService.save(course);
    }

    public void removeCourse(Long categoryId, Long courseId) {
        Category category = getCategory(categoryId);

        Course course = courseService.getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");

        category.getCourses().removeIf(c -> c.getId().equals(courseId));
        course.getCategories().remove(category);
        save(category);
//        courseService.save(course);
    }

    public Category getCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) throw new SearchNotFoundException("Category not found");
        return optionalCategory.get();
    }

    public Category updateCategory(Long categoryId, CategoryDto dto) {
        Category category = getCategory(categoryId);
        category.setName(dto.getName());
        return save(category);
    }

    public List<CategorySearchResponse> search(String keyword) {
        List<Category> categories = categoryRepository.search(keyword);
        List<CategorySearchResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            CategorySearchResponse response = new CategorySearchResponse();
            response.setCategoryId(category.getId());
            response.setName(category.getName());
            responses.add(response);
        }
        return responses;
    }

    public void delete(Category c) {
        for (Course course : c.getCourses()) {
            course.getCategories().remove(c);
        }
        c.getCourses().clear();
        categoryRepository.delete(c);
    }
}
