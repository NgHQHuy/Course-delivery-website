package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.entity.Agenda;
import com.example.courseservice.entity.Chapter;
import com.example.courseservice.entity.Course;
import com.example.courseservice.response.BaseResponse;
import com.example.courseservice.service.AgendaService;
import com.example.courseservice.service.ChapterService;
import com.example.courseservice.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private ChapterService chapterService;

    @Autowired
    private AgendaService agendaService;

    @Operation(summary = "Thêm/sửa thông tin liên quan khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveCourse(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin về khóa học bao gồm khóa học, chương và nội dung từng chương. Thêm id của khóa học, chương hoặc nội dung chương vào để chỉnh sửa thông tin. Bỏ qua id nếu thêm mới")
            @RequestBody CourseUploadRequest requestBody) {
        List<ChapterUploadRequest> chapters = requestBody.getChapters();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Course course = null;
        if (requestBody.getId() != null) {
            course = courseService.getOne(requestBody.getId());
        }

        if (course == null) {
            course = new Course();
            course.setCreatedAt(timestamp);
        }
        course.setName(requestBody.getName());
        course.setDescription(requestBody.getDescription());
        course.setUpdatedAt(timestamp);
        course.setType("course");

        Set<Chapter> chapterSet = new HashSet<>();

        for (ChapterUploadRequest chapterDto : chapters) {
            Chapter chapter = null;
            if (chapterDto.getId() != null) {
                chapter = chapterService.getOne(chapterDto.getId());
            }

            if (chapter == null) {
                chapter = new Chapter();
                chapter.setCreatedAt(timestamp);
            }
            chapter.setName(chapterDto.getName());
            chapter.setDescription(chapterDto.getDescription());
            chapter.setPosition(chapterDto.getPosition());
            chapter.setCourse(course);
            chapter.setUpdatedAt(timestamp);
            chapter.setType("chapter");

            Set<Agenda> agendaSet = new HashSet<>();

            for (AgendaUploadRequest agendaDto : chapterDto.getAgendas()) {
                Agenda agenda = null;
                if (agendaDto.getId() != null) {
                    agenda = agendaService.getOne(agendaDto.getId());
                }
                if (agenda == null) {
                    agenda = new Agenda();
                    agenda.setCreatedAt(timestamp);
                }
                agenda.setName(agendaDto.getName());
                agenda.setDescription(agendaDto.getDescription());
                agenda.setPosition(agendaDto.getPosition());
                agenda.setUpdatedAt(timestamp);
                agenda.setChapter(chapter);
                agenda.setLink(agendaDto.getLink());
                agenda.setType("agenda");
                agendaSet.add(agenda);
            }
            chapter.setAgendas(agendaSet);
            chapterSet.add(chapter);
        }
        course.setChapters(chapterSet);
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
        return ResponseEntity.ok(mappedToParentCourse(courseDtos));
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
    public ResponseEntity<List<ChapterUploadRequest>> getSyllabus(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        Course course = courseService.getOne(id);
        List<ChapterUploadRequest> chapters = new ArrayList<>();
        for (Chapter chapter: course.getChapters()) {
            ChapterUploadRequest chapterDto = new ChapterUploadRequest();
            List<AgendaUploadRequest> agendas = new ArrayList<>();
            for (Agenda agenda : chapter.getAgendas()) {
                AgendaUploadRequest agendaDto = new AgendaUploadRequest();
                agendaDto.setId(agenda.getId());
                agendaDto.setName(agenda.getName());
                agendaDto.setDescription(agenda.getDescription());
                agendaDto.setPosition(agenda.getPosition());
                agendaDto.setLink(agenda.getLink());
                agendas.add(agendaDto);
            }
            chapterDto.setAgendas(agendas);
            chapterDto.setId(chapter.getId());
            chapterDto.setName(chapter.getName());
            chapterDto.setDescription(chapter.getDescription());
            chapterDto.setPosition(chapter.getPosition());
            chapters.add(chapterDto);
        }
        return ResponseEntity.ok(chapters);
    }

    @Operation(summary = "Xóa khóa học")
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> deleteCourse(@Parameter(description = "id của khóa học")
            @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(new MessageResponse(0, "Success"));
    }


    private List<CourseDto> mappedToParentCourse(List<CourseDto> courses) {
        List<CourseDto> temp = new ArrayList<>();

        for (CourseDto course : courses) {
            if (course.getType().equals("course")) {
                temp.add(course);
            }
        }
        return temp;
    }

    private CourseDto mappedToCourseDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        dto.setType(course.getType());
        return dto;
    }
}
