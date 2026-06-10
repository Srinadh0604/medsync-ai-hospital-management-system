package com.srinadh.medsync.controller;

import com.srinadh.medsync.dto.AppointmentDTO;
import com.srinadh.medsync.entity.Appointment;
import com.srinadh.medsync.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public Appointment bookAppointment(@RequestBody AppointmentDTO dto) {
        return service.bookAppointment(dto);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAllAppointments();
    }
}
