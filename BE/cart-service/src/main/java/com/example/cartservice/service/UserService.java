package com.example.cartservice.service;

import com.example.cartservice.dto.CourseDto;
import com.example.cartservice.dto.UserDto;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(CourseService.class);
    private WebClient webClient;

    public UserDto getUser(Long userId) {
        UserDto userDto = null;

        try {
            userDto = webClient.get()
                    .uri("http://localhost:8082/api/user/" + userId)
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(UserDto.class);
                        } else {
                            return response.createException().flatMap(Mono::error);
                        }
                    }).block();
        } catch (Exception ex) {
            logger.error("Response from remote server: " + ex.getMessage());
        }

        return userDto;
    }
}
