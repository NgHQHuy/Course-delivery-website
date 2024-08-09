package com.example.learningservice.service;

import com.example.learningservice.entity.UserProgress;
import com.example.learningservice.repository.UserProgressRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class UserProgressService {
    private UserProgressRepository userProgressRepository;
    private WebClient webClient;
    private static Logger logger = LoggerFactory.getLogger(UserProgressService.class);

    public UserProgress save(UserProgress userProgress) {
        return userProgressRepository.save(userProgress);
    }

    public List<UserProgress> getAllProgressByUser(Long uid) {
        return userProgressRepository.findAllByUserId(uid);
    }

    public List<UserProgress> getAllProgressByUserForCourse(Long userId, Long courseId) {
        return userProgressRepository.findAllByUserIdAndCourseId(userId, courseId);
    }

    private boolean checkForResponse(String uri) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        try {
            //https://stackoverflow.com/a/68168585
            webClient.get()
                    .uri(uri)
                    .retrieve()
                    .toBodilessEntity().block();
            isValid.set(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return isValid.get();
    }

    private boolean checkPostResponse(String uri, Map<String, Long> body) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        try {
            //https://stackoverflow.com/a/68168585
            webClient.post()
                    .uri(uri)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .toBodilessEntity().block();
            isValid.set(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return isValid.get();
    }

//    public boolean isUserRegisteredCourse(Long userId, Long courseId) {
//        Map<String, Long> body = new HashMap<>();
//        body.put("userId", userId);
//        body.put("courseId", courseId);
//        String uri = "http://localhost:8085/api/user-course/checkRegistered";
//        return checkPostResponse(uri, body);
//    }

    public boolean isProgressAlreadyInDB(Long userId, Long courseId) {
        return userProgressRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    public UserProgress findOne(Long userId, Long lectureId) {
        return userProgressRepository.findByUserIdAndLectureId(userId, lectureId);
    }
}
