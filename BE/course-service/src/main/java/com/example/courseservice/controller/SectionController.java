package com.example.courseservice.controller;

import com.example.courseservice.dto.SectionDto;
import com.example.courseservice.entity.Section;
import com.example.courseservice.service.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/section")
@AllArgsConstructor
public class SectionController {

    private SectionService sectionService;

    @GetMapping("{id}")
    public ResponseEntity<SectionDto> getOneSection(@PathVariable Long id) {
        SectionDto sectionDto = new SectionDto();
        Section section = sectionService.getOne(id);
        if (section == null) return ResponseEntity.status(404).build();
        sectionDto.setId(section.getId());
        sectionDto.setName(section.getName());
        sectionDto.setDescription(section.getDescription());
        sectionDto.setCreatedAt(section.getCreatedAt());
        sectionDto.setUpdatedAt(section.getUpdatedAt());
        return ResponseEntity.ok(sectionDto);
    }
}
