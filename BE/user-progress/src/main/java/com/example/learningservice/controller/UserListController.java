package com.example.learningservice.controller;

import com.example.learningservice.dto.*;
import com.example.learningservice.entity.UserList;
import com.example.learningservice.entity.UserListCourse;
import com.example.learningservice.entity.UserProgress;
import com.example.learningservice.exception.SearchNotFoundException;
import com.example.learningservice.service.CourseService;
import com.example.learningservice.service.UserListService;
import com.example.learningservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/user-list")
@AllArgsConstructor
public class UserListController {
    private UserListService userListService;
    private UserService userService;
    private CourseService courseService;

    @PostMapping("create")
    public ResponseEntity<UserList> createList(@Valid @RequestBody CreateListRequest request) {
        if (!userService.isValidUser(request.getUserId())) throw new SearchNotFoundException("User not found");

        UserList createdList = userListService.createList(request.getUserId(), request.getName(), request.getDescription());
        return ResponseEntity.ok(createdList);
    }

    @PostMapping("update")
    public ResponseEntity<UserList> updateList(@Valid @RequestBody ModifyListRequest request) {
        UserList list = new UserList();
        list.setName(request.getName());
        list.setDescription(request.getDescription());
        list.setId(request.getListId());
        UserList modifiedList = userListService.updateList(list);
        return ResponseEntity.ok(modifiedList);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<ListDto>> viewUserLists(@PathVariable Long userId) {
        List<ListDto> listDtos = userListService.viewAllUserList(userId);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("list/{listId}/courses")
    public ResponseEntity<Set<UserListCourse>> viewAllCourse(@PathVariable Long listId) {
        Set<UserListCourse> courses = userListService.viewCourseList(listId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("addOne")
    public ResponseEntity<Set<ListCourseDto>> addOneCourse(@Valid @RequestBody AddOneCourseRequest request) {
        if (!courseService.isValidCourse(request.getCourseId())) throw new SearchNotFoundException("Course not found");
        Set<ListCourseDto> courses = userListService.addOne(request.getListId(), request.getCourseId());
        return ResponseEntity.ok(courses);
    }

    @PostMapping("addMany")
    public ResponseEntity<Set<ListCourseDto>> addManyCourse(@Valid @RequestBody AddManyCourseRequest request) {
        Set<ListCourseDto> courses = userListService.addMany(request.getListId(), request.getCourses());
        return ResponseEntity.ok(courses);
    }

    @DeleteMapping("list/{listId}/delete")
    public ResponseEntity<?> deleteOneList(@PathVariable Long listId) {
        userListService.deleteList(listId);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("deleteCourse")
    public ResponseEntity<?> deleteCourseFromList(@Valid @RequestBody AddOneCourseRequest request) {
        if (!courseService.isValidCourse(request.getCourseId())) throw new SearchNotFoundException("Course not found");
        userListService.removeOneCourse(request.getListId(), request.getCourseId());
        return ResponseEntity.status(200).build();
    }

}
