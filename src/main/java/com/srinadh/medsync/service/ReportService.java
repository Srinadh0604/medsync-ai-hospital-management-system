package com.srinadh.medsync.service;

import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private static final String DISCHARGE_SUMMARY_TEMPLATE = "discharge-summary";

    private final PatientRepository patientRepository;
    private final GeminiService geminiService;
    private final PdfService pdfService;
    private final S3Service s3Service;
    private final EmailService emailService;
    private final AuditService auditService;

    public ReportService(
            PatientRepository patientRepository,
            GeminiService geminiService,
            PdfService pdfService,
            S3Service s3Service,
            EmailService emailService,
            AuditService auditService) {
        this.patientRepository = patientRepository;
        this.geminiService = geminiService;
        this.pdfService = pdfService;
        this.s3Service = s3Service;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    public String generateAndSendReport(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        String aiSummary = geminiService.generatePatientSummary(patientId);
        log.info("Generated AI summary for patientId={}: {}", patientId, aiSummary);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("patientName", patient.getName());
        dataMap.put("disease", patient.getDisease());
        dataMap.put("doctorName", patient.getDoctor() != null ? patient.getDoctor().getName() : "N/A");
        dataMap.put("prescription", patient.getPrescription() != null ? patient.getPrescription() : "N/A");
        dataMap.put("aiSummary", aiSummary);

        byte[] pdfBytes = pdfService.generatePdfBytes(DISCHARGE_SUMMARY_TEMPLATE, dataMap);

        String fileName = "patient-" + patientId + ".pdf";
        String s3Url = s3Service.uploadPdf(fileName, pdfBytes);

        String emailBody = "Hello " + patient.getName() + ",\n\n"
                + "Your AI-generated discharge report is ready.\n\n"
                + "Download Report:\n"
                + s3Url + "\n\n"
                + "Regards,\n"
                + "MedSync Healthcare";

//        emailService.sendEmail(
//                patient.getEmail(),
//                "Your MedSync Discharge Report",
//                emailBody
//        );
        try {

            emailService.sendEmail(
                    patient.getEmail(),
                    "Your MedSync Discharge Report",
                    emailBody
            );

        } catch (Exception e) {

            log.error("Email sending failed", e);
        }

        log.info("Report generated and emailed for patientId={}, url={}", patientId, s3Url);
        auditService.logAction(patient.getEmail(), "GENERATE_REPORT");
        return s3Url;
    }
}
