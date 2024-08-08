package com.example.learningservice.service;

import com.example.learningservice.entity.UserCourse;
import com.example.learningservice.repository.UserCourseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class UserCourseService {
    private UserCourseRepository repository;
    private CourseService courseService;
    private WebClient webClient;
    private static Logger logger = LoggerFactory.getLogger(UserCourseService.class);

    public UserCourse save(UserCourse uc) {
        return repository.save(uc);
    }

    public List<UserCourse> getAllByUserID(Long userId) {
        return repository.findAllByUserId(userId);
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
            logger.error("Response from remote server: " + ex.getMessage());
        }

        return isValid.get();
    }

    public void updateCourseNumOfStudent(Long courseId) {
        Long numberOfStudent = repository.countAllByCourseId(courseId);
        courseService.updateNumberofStudent(courseId, numberOfStudent);
    }

    public boolean isUserRegisteredCourse(Long userId, Long courseId) {
        return repository.existsByUserIdAndCourseId(userId, courseId);
    }

    public UserCourse findOne(Long userId, Long courseId) {
        return repository.findByUserIdAndCourseId(userId, courseId);
    }
}
