package com.srinadh.medsync.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @NotBlank(message = "Disease must not be blank")
    private String disease;

    @NotBlank(message = "Prescription is required")
    private String prescription;

    @Email(message = "Email should be valid")
    private String email;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

