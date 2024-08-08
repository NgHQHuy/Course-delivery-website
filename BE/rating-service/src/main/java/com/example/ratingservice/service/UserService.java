package com.example.ratingservice.service;

import com.example.ratingservice.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private Util util;

    public boolean isValidUser(Long userId) {
        String uri = "http://localhost:8082/api/user/" + userId;
        return util.checkForResponse(uri);
    }
}
