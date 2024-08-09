package com.example.learningservice.service;

import com.example.learningservice.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.stylesheets.LinkStyle;
import com.example.learningservice.dto.SectionSyllabus;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private Util util;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean isValidUser(Long userId) {
        String uri = "http://localhost:8082/api/user/" + userId;
        return util.checkForResponse(uri);
    }
}
