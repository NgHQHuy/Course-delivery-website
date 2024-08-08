package com.example.userservice.service;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserProfileDto;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserProfile;
import com.example.userservice.enums.RoleEnum;
import com.example.userservice.exception.AuthErrorException;
import com.example.userservice.exception.SearchNotFoundException;
import com.example.userservice.exception.UserAlreadyRegisteredException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserProfileRepository;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserProfileRepository userProfileRepository;

    public User save(User u) {
        return userRepository.save(u);
    }

    public void addUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) throw new UserAlreadyRegisteredException("User already registered");
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) throw new UserAlreadyRegisteredException("This email already registered");
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        Optional<Role> optionalRole = roleRepository.findByName(request.getRole());
        user.setRole(optionalRole.get());
        save(user);
    }

    public UserProfile updateProfile(UserProfileDto profileDto) {
        Optional<User> optionalUser = userRepository.findById(profileDto.getUserId());
        if (optionalUser.isEmpty()) throw new SearchNotFoundException("User not found");
        User user = optionalUser.get();
        UserProfile userProfile = user.getProfile();
        userProfile.setName(profileDto.getName());
        userProfile.setGender(profileDto.getGender());
        userProfile.setAvatar(profileDto.getAvatar());
        userProfile.setPhone(profileDto.getPhone());
        userProfile.setAge(profileDto.getAge());
        return save(user).getProfile();
    }

    public User findUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        return result.orElse(null);
    }

    public UserProfile getUserProfile(String username) {
        User user = findUsername(username);
        return user.getProfile();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    public List<UserProfile> search(String keyword) {
        return userProfileRepository.search(keyword);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new SearchNotFoundException("User not found");
        userRepository.deleteById(userId);
    }
}
