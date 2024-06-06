package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User save(User u) {
        return userRepository.save(u);
    }

    public User findUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        return result.orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
