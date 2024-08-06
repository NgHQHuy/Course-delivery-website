package com.example.courseservice.service;

import com.example.courseservice.dto.SectionDto;
import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.Lecture;
import com.example.courseservice.entity.Section;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepository courseRepository;
    private SectionService sectionService;
    private LectureService lectureService;

    public Course save(Course data) {
        return courseRepository.save(data);
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
}
