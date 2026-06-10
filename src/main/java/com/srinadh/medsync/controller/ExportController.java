package com.srinadh.medsync.controller;

import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.dto.DashboardStats;
import com.srinadh.medsync.repository.AuditLogRepository;
import com.srinadh.medsync.repository.PatientRepository;
import com.srinadh.medsync.service.DashboardService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    private final PatientRepository patientRepository;
    private final AuditLogRepository auditLogRepository;
    private final DashboardService dashboardService;

    public ExportController(
            PatientRepository patientRepository,
            AuditLogRepository auditLogRepository,
            DashboardService dashboardService) {

        this.patientRepository = patientRepository;
        this.auditLogRepository = auditLogRepository;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/patients")
    public ResponseEntity<String> exportPatients() {

        List<Patient> patients =
                patientRepository.findAll();

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "ID,Name,Email,Disease\n");

        for (Patient patient : patients) {

            csv.append(patient.getId())
                    .append(",")
                    .append(patient.getName())
                    .append(",")
                    .append(patient.getEmail())
                    .append(",")
                    .append(patient.getDisease())
                    .append("\n");
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=patients.csv")
                .contentType(
                        MediaType.TEXT_PLAIN)
                .body(csv.toString());
    }
    @GetMapping("/audit-logs")
    public ResponseEntity<String> exportAuditLogs() {

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "ID,User,Action,Timestamp\n");

        auditLogRepository.findAll()
                .forEach(log -> {

                    csv.append(log.getId())
                            .append(",")
                            .append(log.getUsername())
                            .append(",")
                            .append(log.getAction())
                            .append(",")
                            .append(log.getTimestamp())
                            .append("\n");
                });

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=audit-logs.csv")
                .body(csv.toString());
    }
    @GetMapping("/dashboard")
    public ResponseEntity<String> exportDashboard() {

        DashboardStats stats =
                dashboardService.getStats();

        String csv =
                """
                Metric,Value
                Total Patients,%d
                Total Doctors,%d
                Total Users,%d
                Total Reports,%d
                Total Audit Logs,%d
                """
                        .formatted(
                                stats.getTotalPatients(),
                                stats.getTotalDoctors(),
                                stats.getTotalUsers(),
                                stats.getTotalReportsGenerated(),
                                stats.getTotalAuditLogs());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=dashboard.csv")
                .body(csv);
    }
}