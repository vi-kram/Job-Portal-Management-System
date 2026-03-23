package com.capg.userservice.controller;

import com.capg.userservice.dto.request.UserLoginRequest;
import com.capg.userservice.dto.request.UserRegisterRequest;
import com.capg.userservice.dto.response.UserResponse;
import com.capg.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest request) {

        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @Valid @RequestBody UserLoginRequest request) {

        String token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }

//    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<String> getLoggedInUser(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        return ResponseEntity.ok("Email: " + email + " Role: " + role);
    }
    
    @GetMapping("/admin/test")
    public String adminTest() {
        return "Admin Access Success";
    }
}