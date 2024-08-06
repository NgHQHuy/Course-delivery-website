package com.example.courseservice.mapper;

import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.LectureDto;
import com.example.courseservice.dto.SectionDto;
import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.Lecture;
import com.example.courseservice.entity.Section;

public class CourseMapper {
    public static CourseDto mappedToCourseDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        dto.setPrice(course.getPrice());
        dto.setSummary(course.getSummary());
        dto.setRequirements(course.getRequirements());
        dto.setInstructorId(course.getInstructor().getId());
        return dto;
    }

    public static SectionDto mappedToSectionDto(Section section) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setId(section.getId());
        sectionDto.setTitle(section.getTitle());
        sectionDto.setDescription(section.getDescription());
        sectionDto.setCreatedAt(section.getCreatedAt());
        sectionDto.setUpdatedAt(section.getUpdatedAt());
        sectionDto.setPosition(section.getPosition());
        return sectionDto;
    }

    public static LectureDto mappedToLectureDto(Lecture lecture) {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(lecture.getId());
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setType(lecture.getType());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setCreatedAt(lecture.getCreatedAt());
        lectureDto.setUpdatedAt(lecture.getUpdatedAt());
        lectureDto.setPosition(lecture.getPosition());
        lectureDto.setSectionId(lecture.getSection().getId());
        lectureDto.setValue(lecture.getValue());
        lectureDto.setLength(lecture.getLength());
        return lectureDto;
    }
}
