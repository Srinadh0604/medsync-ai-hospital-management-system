package com.srinadh.medsync.controller;

import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.PatientRepository;
import com.srinadh.medsync.service.GeminiService;
import com.srinadh.medsync.service.PdfService;
import com.srinadh.medsync.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private static final Logger log = LoggerFactory.getLogger(PdfController.class);
    private static final String DISCHARGE_SUMMARY_TEMPLATE = "discharge-summary";
    private static final String AI_SUMMARY_FALLBACK = "AI summary unavailable at this time.";

    private final PdfService pdfService;
    private final PatientRepository patientRepository;
    private final S3Service s3Service;
    private final GeminiService geminiService;

    public PdfController(
            PdfService pdfService,
            PatientRepository patientRepository,
            S3Service s3Service,
            GeminiService geminiService) {
        this.pdfService = pdfService;
        this.patientRepository = patientRepository;
        this.s3Service = s3Service;
        this.geminiService = geminiService;
    }

    @GetMapping("/download/{patientId}")
    public ResponseEntity<String> downloadPdf(@PathVariable Long patientId) {
        GeneratedPdf generatedPdf = generatePatientPdf(patientId);

        String fileName = "patient-" + generatedPdf.patient().getId() + ".pdf";

        String url = s3Service.uploadPdf(fileName, generatedPdf.pdfBytes());
        log.info("PDF uploaded to S3 for patientId={}, fileName={}", patientId, fileName);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/test/{patientId}")
    public ResponseEntity<byte[]> testPdf(@PathVariable Long patientId) {
        GeneratedPdf generatedPdf = generatePatientPdf(patientId);

        String filename = "discharge-summary-" + patientId + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(generatedPdf.pdfBytes());
    }

//    @GetMapping("/url/{patientId}")
//    public ResponseEntity<Void> getPdfUrl(
//            @PathVariable Long patientId) {
//
//        String fileName = "patient-" + patientId + ".pdf";
//
//        String url = s3Service.generatePresignedUrl(fileName);
//
//        return ResponseEntity
//                .status(302)
//                .header(HttpHeaders.LOCATION, url)
//                .build();
//    }

    @GetMapping("/url/{patientId}")
    public String getPdfUrl(@PathVariable Long patientId) {
        String fileName = "patient-" + patientId + ".pdf";
//        return s3Service.generatePresignedUrl(fileName);

        String url = s3Service.generatePresignedUrl(fileName);

        System.out.println("Generated URL = " + url);

        return url;

    }

    private GeneratedPdf generatePatientPdf(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        String aiSummary = resolveAiSummary(patientId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("patientName", patient.getName());
        variables.put("disease", patient.getDisease());
        variables.put("doctorName", patient.getDoctor() != null ? patient.getDoctor().getName() : "N/A");
        variables.put("prescription", patient.getPrescription() != null ? patient.getPrescription() : "N/A");
        variables.put("aiSummary", aiSummary);

        log.debug("PDF template variables for patientId={}: keys={}, aiSummaryPresent={}",
                patientId, variables.keySet(), variables.containsKey("aiSummary") && variables.get("aiSummary") != null);

        byte[] pdfBytes = pdfService.generatePdfBytes(DISCHARGE_SUMMARY_TEMPLATE, variables);
        log.debug("PDF generated for patientId={}, size={} bytes", patientId, pdfBytes.length);

        return new GeneratedPdf(patient, aiSummary, pdfBytes);
    }

    private String resolveAiSummary(Long patientId) {
        try {
            String aiSummary = geminiService.generatePatientSummary(patientId);
            if (aiSummary == null || aiSummary.isBlank()) {
                log.warn("Gemini returned empty summary for patientId={}, using fallback", patientId);
                return AI_SUMMARY_FALLBACK;
            }
            log.debug("Generated aiSummary for patientId={}: {}", patientId, aiSummary);
            return aiSummary;
        } catch (Exception ex) {
            log.warn("Gemini AI failed for patientId={}, using fallback summary", patientId, ex);
            return AI_SUMMARY_FALLBACK;
        }
    }

    private record GeneratedPdf(Patient patient, String aiSummary, byte[] pdfBytes) {}
}
