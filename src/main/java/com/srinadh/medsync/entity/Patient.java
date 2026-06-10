package com.srinadh.medsync.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
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

    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @NotBlank(message = "Disease must not be blank")
    private String disease;

    private String prescription;

    @Email(message = "Email should be valid")
    private String email;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

