package com.srinadh.medsync.controller;

import com.srinadh.medsync.dto.AppointmentDTO;
import com.srinadh.medsync.entity.Appointment;
import com.srinadh.medsync.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public Appointment bookAppointment(
            @Valid @RequestBody AppointmentDTO dto) {
        return service.bookAppointment(dto);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAllAppointments();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        service.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }
}
