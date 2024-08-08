package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.*;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

import static com.example.courseservice.mapper.CourseMapper.*;

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

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Thêm thông tin liên quan khóa học")
    @PostMapping("create")
    public ResponseEntity<BaseResponse> createCourse(@Valid @RequestBody AddCourseRequest requestBody) {
        courseService.addCourse(requestBody);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @PostMapping("update")
    public ResponseEntity<BaseResponse> updateCourse(@Valid @RequestBody CourseDto dto) {
        courseService.updateCourse(dto);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @PostMapping("{courseId}/addSection")
    public ResponseEntity<List<SectionDto>> addSection(@PathVariable Long courseId, @Valid @RequestBody AddSectionRequest request) {
        Course course = courseService.getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");
        int currentTotal = course.getCourseNumber().getTotalSections();
        Section section = new Section();
        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setPosition(currentTotal + 1);
        section.setCourse(course);
        course.getSections().add(section);
        course.getCourseNumber().setTotalSections(currentTotal + 1);
        Course saved = courseService.save(course);

        List<SectionDto> dto = new ArrayList<>();
        for (Section s : saved.getSections()) {
            dto.add(mappedToSectionDto(s));
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("{courseId}/{sectionId}/addLecture")
    public ResponseEntity<List<LectureDto>> addLecture(@PathVariable("courseId") Long courseId, @PathVariable("sectionId") Long sectionId, @Valid @RequestBody AddLectureRequest request) {
        Section section = courseService.getSection(courseId, sectionId);
        if (section == null) throw new SearchNotFoundException("Section not found");
        int currentTotal = section.getTotalLectures();
        Long lectureLength = request.getLength();
        Lecture lecture = new Lecture();
        lecture.setTitle(request.getTitle());
        lecture.setDescription(request.getDescription());
        lecture.setType(request.getType());
        lecture.setLength(lectureLength);
        lecture.setValue(request.getValue());
        lecture.setPosition(currentTotal + 1);
        lecture.setSection(section);
        section.getLectures().add(lecture);
        section.setLength(section.getLength() + lectureLength);
        section.setTotalLectures(currentTotal + 1);
        section.getCourse().getCourseNumber().setLength(section.getCourse().getCourseNumber().getLength() + lectureLength);
        section.getCourse().getCourseNumber().setTotalSections(section.getCourse().getCourseNumber().getTotalSections() + 1);
        Section saved = sectionService.save(section);
        List<LectureDto> dto = new ArrayList<>();
        for (Lecture l : saved.getLectures()) {
            dto.add(mappedToLectureDto(l));
        }
        return ResponseEntity.ok(dto);
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
    public ResponseEntity<List<SectionSyllabus>> getSyllabus(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        Course course = courseService.getOne(id);
        List<SectionSyllabus> sections = new ArrayList<>();
        for (Section section : course.getSections()) {
            SectionSyllabus sectionDto = new SectionSyllabus();
            List<LectureSyllabus> lectures = new ArrayList<>();
            for (Lecture lecture : section.getLectures()) {
                LectureSyllabus lectureDto = new LectureSyllabus();
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
    @DeleteMapping("{id}/delete")
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
        if (lectureDto.getLength() != null) {
            lecture.setLength(lectureDto.getLength());
        }
        if (lectureDto.getType() != null) {
            lecture.setType(lectureDto.getType());
        }

        lecture.setValue(lectureDto.getValue());
        Lecture saved = lectureService.save(lecture);
        return ResponseEntity.ok(mappedToLectureDto(saved));
    }

    @PostMapping("{courseId}/updateNumOfStudent")
    public ResponseEntity<?> updateNumOfStudent(@PathVariable Long courseId, @RequestBody CourseUpdateNumOfStudentRequest request) {
        Course course = courseService.getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");
        course.getCourseNumber().setNumOfStudent(request.getNumOfStudent());
        courseService.save(course);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("{courseId}/overview")
    public ResponseEntity<CourseDetail> getDetails(@PathVariable Long courseId) {
        Course course = courseService.getOne(courseId);
        if (course == null) throw new SearchNotFoundException("Course not found");
        CourseDetail detail = new CourseDetail();
        detail.setId(course.getId());
        detail.setTitle(course.getTitle());
        detail.setDescription(course.getDescription());
        detail.setSummary(course.getSummary());
        detail.setRequirements(course.getRequirements());
        detail.setPrice(course.getPrice());
        detail.setInstructorId(course.getInstructor().getId());
        detail.setTotalSections(course.getCourseNumber().getTotalSections());
        detail.setLength(course.getCourseNumber().getLength());
        detail.setNumOfStudent(course.getCourseNumber().getNumOfStudent());
        detail.setCreatedAt(course.getCreatedAt());
        detail.setUpdatedAt(course.getUpdatedAt());
        return ResponseEntity.ok(detail);
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

    @GetMapping("{courseId}/addSubmission")
    public ResponseEntity<?> addOneToNumberOfStudent(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getOne(courseId);

        if (course == null) throw new SearchNotFoundException("Course not found");

        Long currentNumOfStudent = course.getCourseNumber().getNumOfStudent();
        course.getCourseNumber().setNumOfStudent(currentNumOfStudent + 1);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("search")
    public ResponseEntity<List<CourseSearchResponse>> search(@RequestParam("keyword") String keyword) {
        List<CourseSearchResponse> responses = courseService.search(keyword);
        return ResponseEntity.ok(responses);
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
}
