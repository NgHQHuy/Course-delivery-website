package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.Lecture;
import com.example.courseservice.entity.Section;
import com.example.courseservice.entity.Course;
import com.example.courseservice.response.BaseResponse;
import com.example.courseservice.service.LectureService;
import com.example.courseservice.service.SectionService;
import com.example.courseservice.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private LectureService lectureService;

    @Operation(summary = "Thêm/sửa thông tin liên quan khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveCourse(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin về khóa học bao gồm khóa học, chương và nội dung từng chương. Thêm id của khóa học, chương hoặc nội dung chương vào để chỉnh sửa thông tin. Bỏ qua id nếu thêm mới")
            @RequestBody CourseUploadRequest requestBody) {
        List<SectionUploadRequest> sections = requestBody.getSections();

        Course course = null;
        if (requestBody.getId() != null) {
            course = courseService.getOne(requestBody.getId());
        }

        if (course == null) {
            course = new Course();
        }
        course.setName(requestBody.getName());
        course.setDescription(requestBody.getDescription());

        Set<Section> sectionSet = new HashSet<>();

        for (SectionUploadRequest sectionDto : sections) {
            Section section = null;
            if (sectionDto.getId() != null) {
                section = sectionService.getOne(sectionDto.getId());
            }

            if (section == null) {
                section = new Section();
            }
            section.setName(sectionDto.getName());
            section.setDescription(sectionDto.getDescription());
            section.setPosition(sectionDto.getPosition());
            section.setCourse(course);

            Set<Lecture> lectureSet = new HashSet<>();

            for (LectureUploadRequest lectureDto : sectionDto.getLectures()) {
                Lecture lecture = null;
                if (lectureDto.getId() != null) {
                    lecture = lectureService.getOne(lectureDto.getId());
                }
                if (lecture == null) {
                    lecture = new Lecture();
                }
                lecture.setName(lectureDto.getName());
                lecture.setDescription(lectureDto.getDescription());
                lecture.setPosition(lectureDto.getPosition());
                lecture.setSection(section);
                lecture.setType("video");
                lectureSet.add(lecture);
            }
            section.setLectures(lectureSet);
            sectionSet.add(section);
        }
        course.setSections(sectionSet);
        courseService.save(course);

        return ResponseEntity.ok(new BaseResponse(0, "Success"));
    }

    @Operation(summary = "Lấy thông tin về tất cả khóa học")
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourse() {
        List<Course> courses = courseService.getAll();
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courses) {
            courseDtos.add(mappedToCourseDto(course));
        }
        return ResponseEntity.ok(courseDtos);
    }

    @Operation(summary = "Lấy thông tin về một khóa học")
    @GetMapping("{id}")
    public ResponseEntity<CourseDto> getOneCourse(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        Course course = courseService.getOne(id);
        return ResponseEntity.ok(mappedToCourseDto(course));
    }

    @Operation(summary = "Lấy để cương môn học")
    @GetMapping("{id}/syllabus")
    public ResponseEntity<List<SectionUploadRequest>> getSyllabus(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        Course course = courseService.getOne(id);
        List<SectionUploadRequest> sections = new ArrayList<>();
        for (Section section : course.getSections()) {
            SectionUploadRequest sectionDto = new SectionUploadRequest();
            List<LectureUploadRequest> lectures = new ArrayList<>();
            for (Lecture lecture : section.getLectures()) {
                LectureUploadRequest lectureDto = new LectureUploadRequest();
                lectureDto.setId(lecture.getId());
                lectureDto.setName(lecture.getName());
                lectureDto.setDescription(lecture.getDescription());
                lectureDto.setPosition(lecture.getPosition());
                lectures.add(lectureDto);
            }
            sectionDto.setLectures(lectures);
            sectionDto.setId(section.getId());
            sectionDto.setName(section.getName());
            sectionDto.setDescription(section.getDescription());
            sectionDto.setPosition(section.getPosition());
            sections.add(sectionDto);
        }
        return ResponseEntity.ok(sections);
    }

    @Operation(summary = "Xóa khóa học")
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> deleteCourse(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(new MessageResponse(0, "Success"));
    }


//    private List<CourseDto> mappedToParentCourse(List<CourseDto> courses) {
//        List<CourseDto> temp = new ArrayList<>();
//
//        for (CourseDto course : courses) {
//            if (course.getType().equals("course")) {
//                temp.add(course);
//            }
//        }
//        return temp;
//    }

    private CourseDto mappedToCourseDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        return dto;
    }
}
