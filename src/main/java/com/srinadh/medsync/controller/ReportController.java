package com.srinadh.medsync.controller;

import com.srinadh.medsync.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/send/{patientId}")
    public ResponseEntity<Map<String, String>> sendReport(@PathVariable Long patientId) {
        System.out.println("REPORT ENDPOINT HIT");
        String s3Url = reportService.generateAndSendReport(patientId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Report generated and sent successfully");
        response.put("s3Url", s3Url);

        return ResponseEntity.ok(response);
    }
}
