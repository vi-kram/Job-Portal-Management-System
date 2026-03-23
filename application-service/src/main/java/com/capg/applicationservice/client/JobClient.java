package com.capg.applicationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service", url = "http://localhost:8082")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    Object getJobById(@PathVariable Long id);
}