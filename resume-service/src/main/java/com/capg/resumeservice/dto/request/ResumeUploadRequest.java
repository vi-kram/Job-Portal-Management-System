package com.capg.resumeservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResumeUploadRequest {

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    public ResumeUploadRequest() {}

    public ResumeUploadRequest(Long userId, String fileUrl) {
        this.userId = userId;
        this.fileUrl = fileUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}