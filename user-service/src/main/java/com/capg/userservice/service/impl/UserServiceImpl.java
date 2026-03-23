package com.capg.userservice.service.impl;

import com.capg.userservice.dto.request.UserLoginRequest;
import com.capg.userservice.dto.request.UserRegisterRequest;
import com.capg.userservice.dto.response.UserResponse;
import com.capg.userservice.entity.User;
import com.capg.userservice.exception.EmailAlreadyExistsException;
import com.capg.userservice.exception.UserNotFoundException;
import com.capg.userservice.repository.UserRepository;
import com.capg.userservice.service.UserService;
import com.capg.userservice.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //  Single constructor (Dependency Injection)
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //  REGISTER USER
    @Override
    public UserResponse registerUser(UserRegisterRequest request) {

        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Convert DTO to Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user
        User savedUser = userRepository.save(user);

        // Convert Entity to Response
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    // 🔹 LOGIN USER (JWT GENERATED HERE 🔥)
    @Override
    public String loginUser(UserLoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //  FINAL CHANGE: Return JWT instead of dummy string
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
    
    

    //  GET USER BY ID
    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}