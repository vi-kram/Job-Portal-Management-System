package com.capg.applicationservice.controller;

import com.capg.applicationservice.dto.request.ApplicationRequest;
import com.capg.applicationservice.dto.response.ApplicationResponse;
import com.capg.applicationservice.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(
            @RequestBody ApplicationRequest request,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.ok(service.apply(request, email, role));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @RequestHeader("X-User-Email") String email
    ) {
        return ResponseEntity.ok(service.getMyApplications(email));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicants(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.ok(service.getApplicants(jobId, role));
    }
    
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable UUID applicationId,
            @RequestParam String status,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.ok(service.updateStatus(applicationId, status, role));
    }
}