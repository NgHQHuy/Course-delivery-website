package com.example.courseservice.service;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.*;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.mapper.CourseMapper;
import com.example.courseservice.repository.CategoryRepository;
import com.example.courseservice.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Course saveCourse(CourseUploadRequest requestBody) {
        Set<SectionUploadRequest> sections = requestBody.getSections() == null ? new HashSet<>() : requestBody.getSections();

        Course course = null;
        if (requestBody.getId() != null) {
            course = getOne(requestBody.getId());
        }

        if (course == null) {
            course = new Course();
        }
        course.setTitle(requestBody.getTitle());
        course.setDescription(requestBody.getDescription());
        course.setPrice(requestBody.getPrice());
        course.setSummary(requestBody.getSummary());
        course.setRequirements(requestBody.getRequirements());
        course.setThumbnail(requestBody.getThumbnail());

        Optional<Instructor> optionalInstructor = instructorService.findOne(requestBody.getInstructorId());
        if (optionalInstructor.isEmpty()) {
            throw new SearchNotFoundException("Instructor is not found");
        }
        course.setInstructor(optionalInstructor.get());

        for (Long categoryId : requestBody.getCategoryIds()) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                course.getCategories().add(category);
                category.getCourses().add(course);
            }
        }

        Set<Section> sectionSet = new HashSet<>();
        Long courseLength = 0L;
        int totalLectures = 0;

        for (SectionUploadRequest sectionDto : sections) {
            Section section = null;
            if (sectionDto.getId() != null) {
                section = sectionService.getOne(sectionDto.getId());
            }

            if (section == null) {
                section = new Section();
            }
            section.setTitle(sectionDto.getTitle());
            section.setPosition(sectionDto.getPosition());
            section.setCourse(course);

            Set<Lecture> lectureSet = new HashSet<>();
            Set<LectureUploadRequest> lectureDtos = sectionDto.getLectures() == null ? new HashSet<>() : sectionDto.getLectures();
            Long sectionLength = 0L;
            section.setTotalLectures(lectureDtos.size());
            totalLectures += lectureDtos.size();
            for (LectureUploadRequest lectureDto : lectureDtos) {
                Lecture lecture = null;
                if (lectureDto.getId() != null) {
                    lecture = lectureService.getOne(lectureDto.getId());
                }
                if (lecture == null) {
                    lecture = new Lecture();
                }
                lecture.setTitle(lectureDto.getTitle());
                lecture.setPosition(lectureDto.getPosition());
                lecture.setSection(section);
                lecture.setType(lectureDto.getType());
                lecture.setValue(lectureDto.getValue());
                sectionLength += lectureDto.getLength();
                lecture.setLength(lectureDto.getLength());
                lectureSet.add(lecture);
            }
            section.setLength(sectionLength);
            courseLength += sectionLength;
            section.setLectures(lectureSet);
            sectionSet.add(section);
        }
        course.setSections(sectionSet);
        course.getCourseNumber().setTotalLectures(totalLectures);
        course.getCourseNumber().setLength(courseLength);
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
                course.getCourseNumber().setTotalLectures(course.getCourseNumber().getTotalLectures() - s.getTotalLectures());
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
                        course.getCourseNumber().setTotalLectures(course.getCourseNumber().getTotalLectures() - 1);
                        course.getCourseNumber().setLength(course.getCourseNumber().getLength() - l.getLength());
                        s.getLectures().remove(l);
                        break;
                    }
                }
            }
        }
        save(course);
    }

    public List<CourseDetail> search(String keyword) {
        List<Course> courses = courseRepository.search(keyword);
        List<CourseDetail> responses = new ArrayList<>();
        for (Course course : courses) {
            CourseDetail response = CourseMapper.mappedToCourseDetail(course);
            responses.add(response);
        }
        return responses;
    }
}
