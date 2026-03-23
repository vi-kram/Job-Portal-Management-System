package com.capg.jobservice.controller;

import com.capg.jobservice.dto.request.JobRequest;
import com.capg.jobservice.dto.response.JobResponse;
import com.capg.jobservice.entity.Job;
import com.capg.jobservice.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //  CREATE JOB
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest request,
            @RequestHeader("X-User-Email") String email) {

        JobResponse response = jobService.createJob(request, email);
        return ResponseEntity.ok(response);
    }

    //  GET JOB BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {

        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    //  GET ALL JOBS
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    //  CLOSE JOB
    @PutMapping("/{id}/close")
    public ResponseEntity<Job> closeJob(@PathVariable Long id) {

        Job job = jobService.closeJob(id);
        return ResponseEntity.ok(job);
    }
}