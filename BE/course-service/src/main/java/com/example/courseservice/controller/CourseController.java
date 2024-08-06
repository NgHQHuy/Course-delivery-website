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

        for (Long categoryId : requestBody.getCategoryIds()) {
            Category category = categoryService.getCategory(categoryId);
            course.getCategories().add(category);
            category.getCourses().add(course);
        }

        Set<Section> sectionSet = new HashSet<>();
        Long courseLength = 0L;

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
            Long sectionLength = 0L;
            section.setTotalLectures(lectureDtos.size());
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
        course.getCourseNumber().setTotalSections(sections.size());
        course.getCourseNumber().setLength(courseLength);
        courseService.save(course);

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
