package com.srinadh.medsync.service;

import com.srinadh.medsync.dto.AppointmentDTO;
import com.srinadh.medsync.entity.Appointment;
import com.srinadh.medsync.entity.Doctor;
import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.AppointmentRepository;
import com.srinadh.medsync.repository.DoctorRepository;
import com.srinadh.medsync.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.emailService = emailService;
    }

    public Appointment bookAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus(dto.getStatus());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        emailService.sendEmail(

                patient.getEmail(),

                "Appointment Confirmed",

                "Hello "
                        + patient.getName()
                        + ", your appointment with  "
                        + doctor.getName()
                        + " is confirmed for "
                        + dto.getAppointmentTime()

        );

        return savedAppointment;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
