package com.example.userprogress.service;

import com.example.userprogress.entity.UserProgress;
import com.example.userprogress.repository.UserProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserProgressService {
    private UserProgressRepository userProgressRepository;

    public UserProgress save(UserProgress userProgress) {
        return userProgressRepository.save(userProgress);
    }

    public List<UserProgress> getAllProgressByUser(Long uid) {
        return userProgressRepository.findAllByUserId(uid);
    }
}
