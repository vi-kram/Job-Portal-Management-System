package com.capg.applicationservice.repository;

import com.capg.applicationservice.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findByUserEmail(String userEmail);

    List<Application> findByJobId(Long jobId);

    boolean existsByJobIdAndUserEmail(Long jobId, String userEmail);
}