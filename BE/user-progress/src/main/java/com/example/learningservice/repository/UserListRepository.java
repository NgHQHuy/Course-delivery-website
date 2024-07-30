package com.example.learningservice.repository;

import com.example.learningservice.entity.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserListRepository extends JpaRepository<UserList, Long> {
    List<UserList> findByUserId(Long userId);
}
