package com.srinadh.medsync.service;

import com.srinadh.medsync.entity.AuditLog;
import com.srinadh.medsync.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(
            String username,
            String action) {

        AuditLog log = new AuditLog();

        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}