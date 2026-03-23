package com.capg.searchservice.service;

import com.capg.searchservice.entity.Job;
import com.capg.searchservice.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSearchService {

    private final JobRepository repository;

    public JobSearchService(JobRepository repository) {
        this.repository = repository;
    }

    //  Search by title
    public List<Job> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    //  Search by location
    public List<Job> searchByLocation(String location) {
        return repository.findByLocationContainingIgnoreCase(location);
    }

    // Search by skills
    public List<Job> searchBySkills(String skills) {
        return repository.findBySkillsContainingIgnoreCase(skills);
    }

    //  Combined search
    public List<Job> searchByTitleAndLocation(String title, String location) {
        return repository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(title, location);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return repository.findAll();
    }
}