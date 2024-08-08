package com.example.userservice.repository;

import com.example.userservice.entity.User;
import com.example.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT u FROM UserProfile u WHERE u.name LIKE %?1%")
    List<UserProfile> search(String keyword);
}
