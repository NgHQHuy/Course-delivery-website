package com.example.ratingservice.service;

import com.example.ratingservice.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CourseService {
    private Util util;
    private static Logger logger = LoggerFactory.getLogger(CourseService.class);

    public boolean isValidCourse(Long courseId) {
        String uri = "http://localhost:8081/api/course/" + courseId;
        return util.checkForResponse(uri);
    }

    public void setCourseRating(Long courseId, Double rating) {
        Map<String, Object> body = new HashMap<>();
        body.put("courseId", courseId);
        body.put("rating", rating);
        String uri = "http://localhost:8081/api/course/setRating";
        WebClient.RequestHeadersSpec<?> request = util.createPOSTRequest(uri, body);
        try {
            request.retrieve().toBodilessEntity().block();
        } catch (Exception ex) {
            logger.error("Response from remote server: " + ex.getMessage());
        }
    }
}
