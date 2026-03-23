package com.capg.searchservice.controller;

import com.capg.searchservice.entity.Job;
import com.capg.searchservice.service.JobSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class JobSearchController {

    private final JobSearchService service;

    public JobSearchController(JobSearchService service) {
        this.service = service;
    }

    //  Search by title
    @GetMapping("/title")
    public List<Job> searchByTitle(@RequestParam String keyword) {
        return service.searchByTitle(keyword);
    }

    //  Search by location
    @GetMapping("/location")
    public List<Job> searchByLocation(@RequestParam String location) {
        return service.searchByLocation(location);
    }

    //  Search by skills
    @GetMapping("/skills")
    public List<Job> searchBySkills(@RequestParam String skills) {
        return service.searchBySkills(skills);
    }

    //  Combined search
    @GetMapping("/combined")
    public List<Job> searchCombined(
            @RequestParam String keyword,
            @RequestParam String location) {
        return service.searchByTitleAndLocation(keyword, location);
    }

    //  Get all jobs
    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return service.getAllJobs();
    }
}