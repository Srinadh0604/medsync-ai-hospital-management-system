package com.srinadh.medsync.service;

import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final ReportService reportService;
    private final PatientRepository patientRepository;

    public SchedulerService(ReportService reportService, PatientRepository patientRepository) {
        this.reportService = reportService;
        this.patientRepository = patientRepository;
    }

//    @Scheduled(fixedRate = 30000)
    @Scheduled(cron = "0 0 9 * * *")
    public void sendScheduledReports() {
        log.info("Scheduler Started");

        List<Patient> patients = patientRepository.findAll();
        log.info("Found {} patients for scheduled report generation", patients.size());

        for (Patient patient : patients) {
            try {
                log.info("Generating Report for patientId={}", patient.getId());
                reportService.generateAndSendReport(patient.getId());
                log.info("Report Generated Successfully for patientId={}", patient.getId());
            } catch (Exception ex) {
                log.error("Scheduled report generation failed for patientId={}", patient.getId(), ex);
            }
        }
    }

}
