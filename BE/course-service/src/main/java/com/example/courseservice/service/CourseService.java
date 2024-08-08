package com.example.courseservice.service;

import com.example.courseservice.dto.AddCourseRequest;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.CourseSearchResponse;
import com.example.courseservice.dto.SectionDto;
import com.example.courseservice.entity.*;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.mapper.CourseMapper;
import com.example.courseservice.repository.CategoryRepository;
import com.example.courseservice.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepository courseRepository;
    private SectionService sectionService;
    private LectureService lectureService;
    private InstructorService instructorService;
    private CategoryRepository categoryRepository;

    public Course save(Course data) {
        return courseRepository.save(data);
    }

    public Course addCourse(AddCourseRequest requestBody) {
        Optional<Instructor> optionalInstructor = instructorService.findOne(requestBody.getInstructorId());
        if (optionalInstructor.isEmpty()) throw new SearchNotFoundException("Instructor not found");
        Instructor instructor = optionalInstructor.get();

        Course course = new Course();
        course.setTitle(requestBody.getTitle());
        course.setDescription(requestBody.getDescription());
        course.setSummary(requestBody.getSummary());
        course.setRequirements(requestBody.getRequirements());
        course.setPrice(requestBody.getPrice());
        course.setInstructor(instructor);
        course.setThumbnail(requestBody.getThumbnail());
        for (Long categoryId : requestBody.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId).get();
            course.getCategories().add(category);
        }
        return save(course);
    }

    public void updateCourse(CourseDto dto) {
        Course course = getOne(dto.getId());
        if (course == null) throw new SearchNotFoundException("Course not found");
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setRequirements(dto.getRequirements());
        course.setSummary(dto.getSummary());
        course.setThumbnail(dto.getThumbnails());

        Optional<Instructor> optionalInstructor = instructorService.findOne(dto.getInstructorId());
        if (optionalInstructor.isEmpty()) throw new SearchNotFoundException("Instructor not found");
        Instructor instructor = optionalInstructor.get();
        course.setInstructor(instructor);
        save(course);
    }


    public Course getOne(Long id) {
        if (courseRepository.findById(id).isPresent()) return courseRepository.findById(id).get();
        return null;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public Section getSection(Long courseId, Long sectionId) {
        Course course = getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");

        for (Section section : course.getSections()) {
            if (sectionId.equals(section.getId())) {
                return section;
            }
        }
        return null;
    }

    public Lecture getLecture(Long courseId, Long sectionId, Long lectureId) {
        Section section = getSection(courseId, sectionId);
        if (section == null) throw new SearchNotFoundException("Section is not found or not belong to course");

        for (Lecture lecture : section.getLectures()) {
            if (lectureId.equals(lecture.getId())) {
                return lecture;
            }
        }
        return null;
    }

    public void deleteSection(Long courseId, Long sectionId) {
        Course course = getOne(courseId);
        for (Section s : course.getSections()) {
            if (s.getId().equals(sectionId)) {
                course.getCourseNumber().setTotalSections(course.getCourseNumber().getTotalSections() - 1);
                course.getCourseNumber().setLength(course.getCourseNumber().getLength() - s.getLength());
                course.getSections().remove(s);
                break;
            }
        }
        courseRepository.save(course);
    }

    public void deleteLecture(Long courseId, Long sectionId, Long lectureId) {
        Course course = getOne(courseId);
        if (course == null) {
            throw new SearchNotFoundException("Course not found");
        }

        for (Section s : course.getSections()) {
            if (s.getId().equals(sectionId)) {
                for (Lecture l : s.getLectures()) {
                    if (l.getId().equals(lectureId)) {
                        s.setTotalLectures(s.getTotalLectures() - 1);
                        s.setLength(s.getLength() - l.getLength());
                        course.getCourseNumber().setLength(course.getCourseNumber().getLength() - l.getLength());
                        s.getLectures().remove(l);
                        break;
                    }
                }
            }
        }
        save(course);
    }

    public List<CourseSearchResponse> search(String keyword) {
        List<Course> courses = courseRepository.search(keyword);
        List<CourseSearchResponse> responses = new ArrayList<>();
        for (Course course : courses) {
            CourseSearchResponse response = new CourseSearchResponse();
            response.setCourseId(course.getId());
            response.setTitle(course.getTitle());
            responses.add(response);
        }
        return responses;
    }
}
