package com.example.learningservice.mapper;

import com.example.learningservice.dto.ListCourseDto;
import com.example.learningservice.entity.UserListCourse;

import java.util.HashSet;
import java.util.Set;

public class UserListCourseMapper {
    public static ListCourseDto mapToListCourseDto(UserListCourse lc) {
        ListCourseDto dto = new ListCourseDto();
        dto.setCourseId(lc.getCourseId());
        dto.setId(lc.getId());
        dto.setListId(lc.getList().getId());
        dto.setAddedAt(lc.getAddedAt());
        return dto;
    }

    public static Set<ListCourseDto> mapAllToListCourseDto(Set<UserListCourse> courses) {
        Set<ListCourseDto> dto = new HashSet<>();
        for (UserListCourse course : courses) {
            dto.add(UserListCourseMapper.mapToListCourseDto(course));
        }
        return dto;
    }
}
