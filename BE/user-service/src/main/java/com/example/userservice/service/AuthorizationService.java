package com.example.userservice.service;

import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.enums.RoleEnum;
import com.example.userservice.exception.AuthErrorException;
import com.example.userservice.exception.UserAlreadyRegisteredException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;

    public User addUser(RegisterRequest u) {
        if (userRepository.findByUsername(u.getUsername()).isPresent()) throw new UserAlreadyRegisteredException("User already registered");
        Optional<User> optionalUser = userRepository.findByEmail(u.getEmail());
        if (optionalUser.isPresent()) throw new UserAlreadyRegisteredException("This email already registered");
        User user = new User();
        user.setUsername(u.getUsername());
        user.setPassword(passwordEncoder.encode(u.getPassword()));
        user.setEmail(u.getEmail());
        Optional<Role> optionalRole = roleRepository.findByName(u.getRole());
        if (optionalRole.get().getName() != RoleEnum.USER) throw new AuthErrorException("Invalid role");
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
        );

        return userRepository.findByUsername(request.getUsername()).orElseThrow();
    }
}
