package com.capg.jobservice.service.impl;

import com.capg.jobservice.dto.request.JobRequest;
import com.capg.jobservice.dto.response.JobResponse;
import com.capg.jobservice.dto.JobEvent;
import com.capg.jobservice.entity.Job;
import com.capg.jobservice.exception.JobNotFoundException;
import com.capg.jobservice.repository.JobRepository;
import com.capg.jobservice.service.JobService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RabbitTemplate rabbitTemplate;

    public JobServiceImpl(JobRepository jobRepository, RabbitTemplate rabbitTemplate) {
        this.jobRepository = jobRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public JobResponse createJob(JobRequest request, String email) {

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary()); // ✅ must be Double
        job.setDescription(request.getDescription());

        job.setCreatedBy(email);
        job.setStatus("OPEN");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job saved = jobRepository.save(job);

        // 🔥 SEND EVENT
        try {
            JobEvent event = new JobEvent();
            event.setJobId(saved.getJobId());
            event.setTitle(saved.getTitle());
            event.setDescription(saved.getDescription());
            event.setCompany(saved.getCompany());
            event.setLocation(saved.getLocation());
            event.setSalary(saved.getSalary()); // ✅ must be Double

            rabbitTemplate.convertAndSend("job.queue", event);

            System.out.println("✅ Job event sent to RabbitMQ");

        } catch (Exception e) {
            System.out.println("❌ RabbitMQ send failed: " + e.getMessage());
        }

        return new JobResponse(
                saved.getJobId(),
                saved.getTitle(),
                saved.getCompany(),
                saved.getLocation(),
                saved.getSalary(),
                saved.getDescription(),
                saved.getStatus(),
                saved.getCreatedBy(),
                saved.getCreatedAt()
        );
    }

    @Override
    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));
    }

    @Override
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(job -> new JobResponse(
                        job.getJobId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getSalary(),
                        job.getDescription(),
                        job.getStatus(),
                        job.getCreatedBy(),
                        job.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public Job closeJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        job.setStatus("CLOSED");
        job.setUpdatedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }
}