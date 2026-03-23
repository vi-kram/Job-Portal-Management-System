package com.capg.applicationservice.service.impl;

import com.capg.applicationservice.client.JobClient;
import com.capg.applicationservice.dto.request.ApplicationRequest;
import com.capg.applicationservice.dto.response.ApplicationResponse;
import com.capg.applicationservice.dto.ApplicationEvent;
import com.capg.applicationservice.entity.Application;
import com.capg.applicationservice.entity.ApplicationStatus;
import com.capg.applicationservice.exception.AlreadyAppliedException;
import com.capg.applicationservice.exception.ResourceNotFoundException;
import com.capg.applicationservice.exception.UnauthorizedException;
import com.capg.applicationservice.repository.ApplicationRepository;
import com.capg.applicationservice.service.ApplicationService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repository;
    private final JobClient jobClient;
    private final RabbitTemplate rabbitTemplate;

    public ApplicationServiceImpl(ApplicationRepository repository,
                                  JobClient jobClient,
                                  RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.jobClient = jobClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ApplicationResponse apply(ApplicationRequest request, String email, String role) {

        
        if (!role.equals("JOB_SEEKER")) {
            throw new UnauthorizedException("Only job seekers can apply");
        }

        //  Validate job exists
        try {
            jobClient.getJobById(request.getJobId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Job not found");
        }

        // Prevent duplicate application
        if (repository.existsByJobIdAndUserEmail(request.getJobId(), email)) {
            throw new AlreadyAppliedException("Already applied to this job");
        }

        //  Create application
        Application app = new Application();
        app.setJobId(request.getJobId());
        app.setUserEmail(email);
        app.setStatus(ApplicationStatus.APPLIED);
        app.setAppliedAt(LocalDateTime.now());

        //  Save
        Application saved = repository.save(app);

        //  Publish event (FIXED)
        try {
            ApplicationEvent event = new ApplicationEvent();
            event.setApplicationId(saved.getApplicationId().toString());
            event.setUserEmail(saved.getUserEmail());
            event.setJobId(saved.getJobId().toString());
            event.setStatus(saved.getStatus().name());

            rabbitTemplate.convertAndSend(
                    "application.queue",
                    event
            );

            System.out.println("Event sent to RabbitMQ");

        } catch (Exception e) {
            System.out.println("RabbitMQ not available: " + e.getMessage());
        }

        return map(saved);
    }

    @Override
    public List<ApplicationResponse> getMyApplications(String email) {
        return repository.findByUserEmail(email)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponse> getApplicants(Long jobId, String role) {

        if (!role.equals("RECRUITER")) {
            throw new UnauthorizedException("Only recruiters can view applicants");
        }

        return repository.findByJobId(jobId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponse updateStatus(UUID applicationId, String status, String role) {

        if (!role.equals("RECRUITER")) {
            throw new UnauthorizedException("Only recruiters can update application status");
        }

        Application app = repository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        ApplicationStatus newStatus;
        try {
            newStatus = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid status value");
        }

        if (app.getStatus() == ApplicationStatus.REJECTED) {
            throw new RuntimeException("Cannot update rejected application");
        }

        app.setStatus(newStatus);

        Application updated = repository.save(app);

        return map(updated);
    }

    private ApplicationResponse map(Application app) {
        return new ApplicationResponse(
                app.getApplicationId(),
                app.getJobId(),
                app.getUserEmail(),
                app.getStatus(),
                app.getAppliedAt()
        );
    }
}