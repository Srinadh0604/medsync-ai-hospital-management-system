package com.srinadh.medsync.dto;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long patientId;
    private Long doctorId;

    @Future(message = "Appointment must be in the future")
    private LocalDateTime appointmentTime;

    private AppointmentStatus  status;
}
