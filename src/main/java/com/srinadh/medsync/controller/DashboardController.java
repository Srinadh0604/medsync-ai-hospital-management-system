package com.srinadh.medsync.controller;

import com.srinadh.medsync.dto.DashboardStats;
import com.srinadh.medsync.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStats getStats() {

        return dashboardService.getStats();
    }
}