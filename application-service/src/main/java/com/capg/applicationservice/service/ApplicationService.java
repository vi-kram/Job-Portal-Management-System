package com.capg.applicationservice.service;

import java.util.List;
import java.util.UUID;

import com.capg.applicationservice.dto.request.ApplicationRequest;
import com.capg.applicationservice.dto.response.ApplicationResponse;

public interface ApplicationService {

	ApplicationResponse apply(ApplicationRequest request, String email, String role);

	List<ApplicationResponse> getMyApplications(String email);

	List<ApplicationResponse> getApplicants(Long jobId, String role);
	
	ApplicationResponse updateStatus(UUID applicationId, String status, String role);
}