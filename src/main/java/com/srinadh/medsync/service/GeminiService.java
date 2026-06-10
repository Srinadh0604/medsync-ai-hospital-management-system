package com.srinadh.medsync.service;

import org.springframework.beans.factory.annotation.Value;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final PatientRepository patientRepository;

    public GeminiService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void checkKey() {
//        System.out.println("Gemini Key = " + apiKey);
    }


    public String getHealthSuggestion(String disease) {
        return generateSummary("Give health suggestions for: " + disease);
    }

    public String generateSummary(String prompt) {
        try {
            Client client = Client.builder()
                    .apiKey(apiKey)
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null);

            return response.text();
        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate AI summary", e);

//            System.out.println("Gemini unavailable: " + e.getMessage());

            System.err.println("Gemini API Error: " + e.getMessage());

            return """
            Patient Overview

            AI service is currently unavailable.

            Disease Description

            Please refer to doctor's diagnosis.

            Medication Information

            Follow prescribed medication.

            Health Recommendations

            Take adequate rest and stay hydrated.

            Follow-up Advice

            Contact your doctor if symptoms worsen.
            """;
        }
    }

    public String generatePatientSummary(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        String prescription = patient.getPrescription() != null ? patient.getPrescription() : "N/A";

        String prompt = """
                Create a professional medical patient summary.

                Patient Details:

                Name: %s
                Age: %d
                Disease: %s
                Prescription: %s

                Generate:

                Patient Overview
                Disease Description
                Medication Information
                Health Recommendations
                Follow-up Advice

                IMPORTANT:
                Return plain text only.
                No markdown.
                No asterisks.
                No bullet points.
                Keep under 150 words.
                """
                .formatted(
                        patient.getName(),
                        patient.getAge(),
                        patient.getDisease(),
                        prescription
                );

        return generateSummary(prompt);
    }
}

