package com.capg.resumeservice.service.impl;

import com.capg.resumeservice.dto.request.ResumeUploadRequest;
import com.capg.resumeservice.dto.response.ResumeResponse;
import com.capg.resumeservice.entity.Resume;
import com.capg.resumeservice.repository.ResumeRepository;
import com.capg.resumeservice.service.ResumeService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Override
    public ResumeResponse uploadResume(ResumeUploadRequest request) {

        Resume resume = new Resume();
        resume.setUserId(request.getUserId());
        resume.setFileUrl(request.getFileUrl());
        resume.setUploadedAt(LocalDateTime.now());

        Resume saved = resumeRepository.save(resume);

        return mapToResponse(saved);
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return mapToResponse(resume);
    }

    @Override
    public List<ResumeResponse> getResumesByUserId(Long userId) {

        List<Resume> resumes = resumeRepository.findByUserId(userId);

        return resumes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //  Common mapper (VERY IMPORTANT PRACTICE)
    private ResumeResponse mapToResponse(Resume resume) {
        return new ResumeResponse(
                resume.getResumeId(),
                resume.getUserId(),
                resume.getFileUrl(),
                resume.getUploadedAt()
        );
    }
}