package com.example.cartservice.service;

import com.example.cartservice.dto.CourseDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CourseService {
    private static Logger logger = LoggerFactory.getLogger(CourseService.class);
    private WebClient webClient;

    public CourseDto getCourse(Long courseId) {
        CourseDto courseDto = null;

        try {
            courseDto = webClient.get()
                    .uri("http://localhost:8081/api/course/" + courseId)
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(CourseDto.class);
                        } else {
                            return response.createException().flatMap(Mono::error);
                        }
                    }).block();
        } catch (Exception ex) {
            logger.error("Response from remote server: " + ex.getMessage());
        }

        return courseDto;
    }
}
