package com.capg.jobservice.service;

import com.capg.jobservice.dto.request.JobRequest;
import com.capg.jobservice.dto.response.JobResponse;
import com.capg.jobservice.entity.Job;
import java.util.List;

public interface JobService {

	JobResponse createJob(JobRequest request, String recruiterEmail);

    Job getJobById(Long jobId);

    List<JobResponse> getAllJobs();

    Job closeJob(Long jobId);
}