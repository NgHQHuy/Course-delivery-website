package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.Instructor;
import com.example.courseservice.entity.Lecture;
import com.example.courseservice.entity.Section;
import com.example.courseservice.entity.Course;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.service.InstructorService;
import com.example.courseservice.service.LectureService;
import com.example.courseservice.service.SectionService;
import com.example.courseservice.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private InstructorService instructorService;

    @Operation(summary = "Thêm/sửa thông tin liên quan khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveCourse(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin về khóa học bao gồm khóa học, chương và nội dung từng chương. Thêm id của khóa học, chương hoặc nội dung chương vào để chỉnh sửa thông tin. Bỏ qua id nếu thêm mới")
            @Valid @RequestBody CourseUploadRequest requestBody) {
        List<SectionUploadRequest> sections = requestBody.getSections() == null ? new ArrayList<>() : requestBody.getSections();

        Course course = null;
        if (requestBody.getId() != null) {
            course = courseService.getOne(requestBody.getId());
        }

        if (course == null) {
            course = new Course();
        }
        course.setTitle(requestBody.getTitle());
        course.setDescription(requestBody.getDescription());
        course.setPrice(requestBody.getPrice());
        course.setSummary(requestBody.getSummary());
        course.setRequirements(requestBody.getRequirements());

        Optional<Instructor> optionalInstructor = instructorService.findOne(requestBody.getInstructorId());
        if (optionalInstructor.isEmpty()) {
            throw new SearchNotFoundException("Instructor is not found");
        }
        course.setInstructor(optionalInstructor.get());

        Set<Section> sectionSet = new HashSet<>();

        for (SectionUploadRequest sectionDto : sections) {
            Section section = null;
            if (sectionDto.getId() != null) {
                section = sectionService.getOne(sectionDto.getId());
            }

            if (section == null) {
                section = new Section();
            }
            section.setTitle(sectionDto.getTitle());
            section.setDescription(sectionDto.getDescription());
            section.setPosition(sectionDto.getPosition());
            section.setCourse(course);

            Set<Lecture> lectureSet = new HashSet<>();
            List<LectureUploadRequest> lectureDtos = sectionDto.getLectures() == null ? new ArrayList<>() : sectionDto.getLectures();

            for (LectureUploadRequest lectureDto : lectureDtos) {
                Lecture lecture = null;
                if (lectureDto.getId() != null) {
                    lecture = lectureService.getOne(lectureDto.getId());
                }
                if (lecture == null) {
                    lecture = new Lecture();
                }
                lecture.setTitle(lectureDto.getTitle());
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

        return ResponseEntity.ok(new BaseResponse("Success"));
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
        if (course == null) return ResponseEntity.status(404).build();
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
                lectureDto.setTitle(lecture.getTitle());
                lectureDto.setDescription(lecture.getDescription());
                lectureDto.setPosition(lecture.getPosition());
                lectures.add(lectureDto);
            }
            sectionDto.setLectures(lectures);
            sectionDto.setId(section.getId());
            sectionDto.setTitle(section.getTitle());
            sectionDto.setDescription(section.getDescription());
            sectionDto.setPosition(section.getPosition());
            sections.add(sectionDto);
        }
        return ResponseEntity.ok(sections);
    }

    @Operation(summary = "Xóa khóa học")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCourse(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("{courseId}/{sectionId}")
    public ResponseEntity<SectionDto> getSection(@PathVariable("courseId") Long courseId, @PathVariable("sectionId") Long sectionId) {
        Section section = courseService.getSection(courseId, sectionId);
        if (section == null) throw new SearchNotFoundException("Section is not found or not belong to course");
        return ResponseEntity.ok(mappedToSectionDto(section));
    }

    @GetMapping("{courseId}/{sectionId}/{lectureId}")
    public ResponseEntity<LectureDto> getLecture(@PathVariable("courseId") Long courseId, @PathVariable("sectionId") Long sectionId, @PathVariable("lectureId") Long lectureId) {
        Lecture lecture = courseService.getLecture(courseId, sectionId, lectureId);
        if (lecture == null) throw new SearchNotFoundException("Lecture is not found or not belong to course");
        return ResponseEntity.ok(mappedToLectureDto(lecture));
    }

    @PostMapping("{courseId}/updateSection")
    public ResponseEntity<SectionDto> updateSection(@PathVariable Long courseId, @Valid @RequestBody SectionDto sectionDto) {
        Section section = courseService.getSection(courseId, sectionDto.getId());

        if (section == null) throw new SearchNotFoundException("Section is not found or not belong to course");

        if (sectionDto.getTitle() != null) {
            section.setTitle(sectionDto.getTitle());
        }
        if (sectionDto.getDescription() != null) {
            section.setDescription(sectionDto.getDescription());
        }
        if (sectionDto.getPosition() > 0) {
            section.setPosition(sectionDto.getPosition());
        }
        Section saved = sectionService.save(section);
        return ResponseEntity.ok(mappedToSectionDto(saved));
    }

    @PostMapping("{courseId}/updateLecture")
    public ResponseEntity<LectureDto> updateSection(@PathVariable Long courseId, @Valid @RequestBody LectureDto lectureDto) {
        Lecture lecture = courseService.getLecture(courseId, lectureDto.getSectionId(), lectureDto.getId());

        if (lecture == null) throw new SearchNotFoundException("Lecture is not found or not belong to course");

        if (lectureDto.getTitle() != null) {
            lecture.setTitle(lectureDto.getTitle());
        }
        if (lectureDto.getDescription() != null) {
            lecture.setDescription(lectureDto.getDescription());
        }
        if (lectureDto.getPosition() > 0) {
            lecture.setPosition(lectureDto.getPosition());
        }
        Lecture saved = lectureService.save(lecture);
        return ResponseEntity.ok(mappedToLectureDto(saved));
    }

    @DeleteMapping("{courseId}/{sectionId}/delete")
    public ResponseEntity<?> deleteSection(@PathVariable("courseId") Long courseId, @PathVariable("sectionId") Long sectionId) {
        Section section = courseService.getSection(courseId, sectionId);

        if (section == null) throw new SearchNotFoundException("Section is not found or not belong to course");

        courseService.deleteSection(courseId, sectionId);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("{courseId}/{sectionId}/{lectureId}/delete")
    public ResponseEntity<?> deleteLecture(@PathVariable("courseId") Long courseId, @PathVariable("sectionId") Long sectionId, @PathVariable("lectureId") Long lectureId) {
        Lecture lecture = courseService.getLecture(courseId, sectionId, lectureId);

        if (lecture == null) throw new SearchNotFoundException("Lecture is not found or not belong to course");

        courseService.deleteLecture(courseId, sectionId, lectureId);
        return ResponseEntity.status(200).build();
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

    private SectionDto mappedToSectionDto(Section section) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setId(section.getId());
        sectionDto.setTitle(section.getTitle());
        sectionDto.setDescription(section.getDescription());
        sectionDto.setCreatedAt(section.getCreatedAt());
        sectionDto.setUpdatedAt(section.getUpdatedAt());
        sectionDto.setPosition(section.getPosition());
        return sectionDto;
    }

    private LectureDto mappedToLectureDto(Lecture lecture) {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(lecture.getId());
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setType(lecture.getType());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setCreatedAt(lecture.getCreatedAt());
        lectureDto.setUpdatedAt(lecture.getUpdatedAt());
        lectureDto.setPosition(lecture.getPosition());
        return lectureDto;
    }
}
