package com.example.ratingservice.service;

import com.example.ratingservice.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseService {
    private Util util;

    public boolean isValidCourse(Long courseId) {
        String uri = "http://localhost:8081/api/course/" + courseId;
        return util.checkForResponse(uri);
    }
}
