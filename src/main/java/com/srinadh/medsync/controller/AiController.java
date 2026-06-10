package com.srinadh.medsync.controller;

import com.srinadh.medsync.service.GeminiService;
import com.srinadh.medsync.service.PatientSummaryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final GeminiService geminiService;
    private final PatientSummaryService patientSummaryService;

    public AiController(GeminiService geminiService, PatientSummaryService patientSummaryService) {
        this.geminiService = geminiService;
        this.patientSummaryService = patientSummaryService;
    }

    @GetMapping("/suggestion")
    public String getSuggestion(@RequestParam String disease) {

        System.out.println("AI endpoint hit!");

        return geminiService
                .getHealthSuggestion(disease);
    }

    @GetMapping(value = "/patient-summary/{patientId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getPatientSummary(@PathVariable Long patientId) {
        return patientSummaryService.generatePatientSummary(patientId);
    }
}

