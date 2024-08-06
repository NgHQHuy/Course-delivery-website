package com.example.courseservice.controller;

import com.example.courseservice.dto.LectureDto;
import com.example.courseservice.entity.Lecture;
import com.example.courseservice.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping("{id}")
    public ResponseEntity<LectureDto> getOne(@PathVariable Long id) {
        Lecture lecture = lectureService.getOne(id);
        if (lecture == null) {
            return ResponseEntity.status(404).build();
        }
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(lecture.getId());
        lectureDto.setType(lecture.getType());
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setPosition(lecture.getPosition());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setCreatedAt(lecture.getCreatedAt());
        lectureDto.setUpdatedAt(lecture.getUpdatedAt());
        return ResponseEntity.ok(lectureDto);
    }
}
