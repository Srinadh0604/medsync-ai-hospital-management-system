package com.srinadh.medsync.dto;

public class DashboardStats {

    private long totalPatients;
    private long totalDoctors;
    private long totalUsers;
    private long totalReportsGenerated;
    private long totalAuditLogs;

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalReportsGenerated() {
        return totalReportsGenerated;
    }

    public void setTotalReportsGenerated(long totalReportsGenerated) {
        this.totalReportsGenerated = totalReportsGenerated;
    }

    public long getTotalAuditLogs() {
        return totalAuditLogs;
    }

    public void setTotalAuditLogs(long totalAuditLogs) {
        this.totalAuditLogs = totalAuditLogs;
    }
}
