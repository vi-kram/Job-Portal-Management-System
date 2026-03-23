package com.capg.searchservice.repository;

import com.capg.searchservice.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    //  Search by title (keyword)
    List<Job> findByTitleContainingIgnoreCase(String title);

    //  Search by location
    List<Job> findByLocationContainingIgnoreCase(String location);

    //  Search by skills
    List<Job> findBySkillsContainingIgnoreCase(String skills);

    //  Combined search (title + location)
    List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(String title, String location);
}