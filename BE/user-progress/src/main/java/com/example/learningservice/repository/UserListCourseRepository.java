package com.example.learningservice.repository;

import com.example.learningservice.entity.UserList;
import com.example.learningservice.entity.UserListCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserListCourseRepository extends JpaRepository<UserListCourse, Long> {
    void removeByListIdAndCourseId(Long listId, Long courseId);

    boolean existsByListAndCourseId(UserList list, Long courseId);
}
