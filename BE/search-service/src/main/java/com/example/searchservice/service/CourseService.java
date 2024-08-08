package com.example.searchservice.service;

import com.example.searchservice.dto.Course;
import com.example.searchservice.dto.User;
import com.example.searchservice.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private Util util;
    private static Logger logger = LoggerFactory.getLogger(CourseService.class);


    public List<Course> search(String keyword) {
        String uri = "http://localhost:8081/api/course/search?keyword=" + util.encodeValue(keyword);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = util.createGETRequest(uri);
        try {
            return requestHeadersSpec
                    .exchangeToMono(clientResponse -> clientResponse
                            .bodyToMono(new ParameterizedTypeReference<List<Course>>() {}))
                    .block();
        } catch (Exception ex) {
            logger.error("Response from remote server: " + ex.getMessage());
        }
        return null;
    }
}
