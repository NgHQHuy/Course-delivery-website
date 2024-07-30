package com.example.learningservice.service;

import com.example.learningservice.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class CourseService {
    private Util util;

    public boolean isValidCourse(Long courseId) {
        String uri = "http://localhost:8081/api/course/" + courseId;
        return util.checkForResponse(uri);
    }

    public boolean isValidSection(Long courseId, Long sectionId) {
        String uri = "http://localhost:8081/api/course/" + courseId + "/" + sectionId;
        return util.checkForResponse(uri);
    }

    public boolean isValidLecture(Long courseId, Long sectionId, Long lectureId) {
        String uri = "http://localhost:8081/api/course/" + courseId + "/" + sectionId + "/" + lectureId;
        return util.checkForResponse(uri);
    }
}
