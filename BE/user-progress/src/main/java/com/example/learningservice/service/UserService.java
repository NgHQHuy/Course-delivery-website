package com.example.learningservice.service;

import com.example.learningservice.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import com.example.learningservice.dto.SectionSyllabus;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private Util util;

    public boolean isValidUser(Long userId) {
        String uri = "http://localhost:8082/api/user/" + userId;
        return util.checkForResponse(uri);
    }
}
