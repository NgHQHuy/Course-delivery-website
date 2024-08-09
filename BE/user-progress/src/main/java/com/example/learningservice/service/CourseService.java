package com.example.learningservice.service;

import com.example.learningservice.dto.SectionSyllabus;
import com.example.learningservice.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class CourseService {
    private Util util;
    private static Logger logger = LoggerFactory.getLogger(CourseService.class);

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

    public boolean updateNumberofStudent(Long courseId, Long numberOfStudent) {
        String uri = "http://localhost:8081/api/course/" + courseId + "/updateNumOfStudent";
        Map<String, String> body = new HashMap<>();
        body.put("numOfStudent", String.valueOf(numberOfStudent));
        return util.checkPostRequest(uri, body);
    }

    public List<SectionSyllabus> getCourseSyllabus(Long courseId) {
        String uri = "http://localhost:8081/api/course/" + courseId + "/syllabus";
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = util.createGETRequest(uri);
        try {
            return requestHeadersSpec.exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(new ParameterizedTypeReference<List<SectionSyllabus>>() {});
                } else {
                    return response.createException().flatMap(Mono::error);
                }
            }).block();
        } catch (Exception ex) {
            logger.error("Response from remote server: " + ex.getMessage());
        }
        return null;
    }
}
