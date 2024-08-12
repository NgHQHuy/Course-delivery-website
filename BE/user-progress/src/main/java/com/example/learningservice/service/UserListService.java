package com.example.learningservice.service;

import com.example.learningservice.dto.ListCourseDto;
import com.example.learningservice.dto.ListDto;
import com.example.learningservice.entity.UserList;
import com.example.learningservice.entity.UserListCourse;
import com.example.learningservice.exception.ListAlreadyAddedCourseException;
import com.example.learningservice.exception.SearchNotFoundException;
import com.example.learningservice.mapper.UserListCourseMapper;
import com.example.learningservice.repository.UserListCourseRepository;
import com.example.learningservice.repository.UserListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserListService {
    private UserListRepository listRepository;
    private UserListCourseRepository userListCourseRepository;
    private CourseService courseService;

    public UserList createList(Long userId, String name, String description) {
        UserList list = new UserList();
        list.setUserId(userId);
        list.setName(name);
        list.setDescription(description);
        return listRepository.save(list);
    }

    public List<ListDto> viewAllUserList(Long userId) {
        List<UserList> lists = listRepository.findByUserId(userId);
        List<ListDto> listDtos = new ArrayList<>();
        for (UserList list : lists) {
            ListDto dto = new ListDto();
            dto.setId(list.getId());
            dto.setName(list.getName());
            dto.setDescription(list.getDescription());
            listDtos.add(dto);
        }
        return listDtos;
    }

    public Set<ListCourseDto> viewCourseList(Long listId) {
        Optional<UserList> optionalList = findById(listId);
        if (optionalList.isEmpty()) throw new SearchNotFoundException("List not found");
        Set<UserListCourse> courses = optionalList.get().getCourses();
        Set<ListCourseDto> dtos = UserListCourseMapper.mapAllToListCourseDto(courses);
        return dtos;
    }

    public UserList updateList(UserList modifiedList) {
        Optional<UserList> optionalList = findById(modifiedList.getId());
        if (optionalList.isEmpty()) throw new SearchNotFoundException("List not found");
        UserList list = optionalList.get();
        if (!modifiedList.getName().isBlank()) list.setName(modifiedList.getName());
        if (!modifiedList.getDescription().isBlank()) list.setDescription(modifiedList.getDescription());
        return listRepository.save(list);
    }

    public Optional<UserList> findById(Long listId) {
        return listRepository.findById(listId);
    }

    public Set<ListCourseDto> addOne(Long listId, Long courseId) {
        Optional<UserList> optionalList = findById(listId);
        if (optionalList.isEmpty()) throw new SearchNotFoundException("List not found");
        if (userListCourseRepository.existsByListAndCourseId(optionalList.get(), courseId)) throw new ListAlreadyAddedCourseException("List already added this course");
        UserListCourse lc = new UserListCourse();
        lc.setList(optionalList.get());
        lc.setCourseId(courseId);
        UserList list = optionalList.get();
        list.getCourses().add(lc);
        UserList saved = listRepository.save(list);
        return UserListCourseMapper.mapAllToListCourseDto(saved.getCourses());
    }

    public Set<ListCourseDto> addMany(Long listId, Set<Long> courses) {
        Optional<UserList> optionalList = findById(listId);
        if (optionalList.isEmpty()) throw new SearchNotFoundException("List not found");
        UserList list = optionalList.get();
        for (Long courseId : courses) {
            if (!courseService.isValidCourse(courseId)) continue;
            if (userListCourseRepository.existsByListAndCourseId(optionalList.get(), courseId)) continue;
            UserListCourse lc = new UserListCourse();
            lc.setList(optionalList.get());
            lc.setCourseId(courseId);
            list.getCourses().add(lc);
        }
        UserList saved = listRepository.save(list);
        return UserListCourseMapper.mapAllToListCourseDto(saved.getCourses());
    }

    public void deleteList(Long listId) {
        listRepository.deleteById(listId);
    }

    public void removeOneCourse(Long listId, Long courseId) {
        Optional<UserList> optionalList = findById(listId);
        if (optionalList.isEmpty()) throw new SearchNotFoundException("List not found");
        UserList list = optionalList.get();
        for (UserListCourse course : list.getCourses()) {
            if (course.getCourseId().equals(courseId)) {
                list.getCourses().remove(course);
                listRepository.save(list);
//                userListCourseRepository.removeByListIdAndCourseId(listId, courseId);
                break;
            }
        }
    }
}
