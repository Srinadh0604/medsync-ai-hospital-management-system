package com.srinadh.medsync.controller;

import com.srinadh.medsync.entity.AuditLog;
import com.srinadh.medsync.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    public AuditController(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/logs")
    public List<AuditLog> getLogs() {

        return auditLogRepository.findAll();
    }
}