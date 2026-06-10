package com.srinadh.medsync.dto;

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
    private LocalDateTime appointmentTime;
    private String status;
}
