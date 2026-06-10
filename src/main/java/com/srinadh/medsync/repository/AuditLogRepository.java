package com.srinadh.medsync.repository;

import com.srinadh.medsync.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    long countByAction(String action);
}