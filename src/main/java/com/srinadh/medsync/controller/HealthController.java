package com.srinadh.medsync.controller;

import com.srinadh.medsync.repository.PatientRepository;
import com.srinadh.medsync.service.GeminiService;
import com.srinadh.medsync.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final PatientRepository patientRepository;
    private final GeminiService geminiService;
    private final S3Service s3Service;

    public HealthController(
            PatientRepository patientRepository,
            GeminiService geminiService,
            S3Service s3Service){

        this.patientRepository = patientRepository;
        this.geminiService = geminiService;
        this.s3Service = s3Service;}

    @GetMapping("/health/database")
    public Map<String, Object> databaseHealth() {

        long patientCount =
                patientRepository.count();

        return Map.of(
                "status", "UP",
                "patients", patientCount
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {

        return Map.of(
                "status", "UP",
                "service", "MedSync"
        );
    }
    @GetMapping("/health/ai")
    public Map<String, String> aiHealth() {

        System.out.println("AI Health Endpoint Hit");
        try {

//            geminiService.getHealthSuggestion("fever");
//
//            System.out.println("Gemini Success");
//
//            return Map.of(
//                    "status",
//                    "UP");


            return Map.of(
                    "status",
                    "UP",
                    "provider",
                    "Gemini");

        } catch (Exception ex) {

            return Map.of(
                    "status",
                    "DOWN",
                    "error",
                    ex.getMessage());
        }
    }
    @GetMapping("/health/s3")
    public Map<String, String> s3Health() {

        boolean healthy =
                s3Service.healthCheck();

        return Map.of(
                "status",
                healthy ? "UP" : "DOWN"
        );
    }
    @GetMapping("/health/full")
    public Map<String, Object> fullHealth() {

        return Map.of(
                "application", "UP",
                "database", "UP",
                "s3", s3Service.healthCheck()
                        ? "UP" : "DOWN"
        );
    }
}