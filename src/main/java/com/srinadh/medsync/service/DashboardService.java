package com.srinadh.medsync.service;

import com.srinadh.medsync.dto.DashboardStats;
import com.srinadh.medsync.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            UserRepository userRepository,
            AuditLogRepository auditLogRepository) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public DashboardStats getStats() {

        DashboardStats stats =
                new DashboardStats();

        stats.setTotalPatients(
                patientRepository.count());

        stats.setTotalDoctors(
                doctorRepository.count());

        stats.setTotalUsers(
                userRepository.count());

        stats.setTotalAuditLogs(
                auditLogRepository.count());

        stats.setTotalReportsGenerated(
                auditLogRepository
                        .countByAction(
                                "GENERATE_REPORT"));

        return stats;
    }
}