package com.srinadh.medsync.service;

import org.springframework.stereotype.Service;

@Service
public class PatientSummaryService {

    private final GeminiService geminiService;

    public PatientSummaryService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String generatePatientSummary(Long patientId) {
        return geminiService.generatePatientSummary(patientId);
    }
}
