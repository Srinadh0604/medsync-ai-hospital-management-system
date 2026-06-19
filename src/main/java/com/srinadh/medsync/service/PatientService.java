package com.srinadh.medsync.service;

import com.srinadh.medsync.dto.PatientDTO;
import com.srinadh.medsync.entity.Doctor;
import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.exception.ResourceNotFoundException;
import com.srinadh.medsync.repository.DoctorRepository;
import com.srinadh.medsync.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repository;
    private final DoctorRepository doctorRepository;

    public PatientService(PatientRepository repository, DoctorRepository doctorRepository) {
        this.repository = repository;
        this.doctorRepository = doctorRepository;
    }

    public Patient save(PatientDTO patientDTO) {
        Doctor doctor = doctorRepository.findById(patientDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + patientDTO.getDoctorId()));

        if(patientDTO.getDoctorId() == null){
            throw new RuntimeException("Doctor ID is required");
        }

        Patient patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setAge(patientDTO.getAge());
        patient.setDisease(patientDTO.getDisease());
        patient.setPrescription(patientDTO.getPrescription());
        patient.setEmail(patientDTO.getEmail());
        patient.setDoctor(doctor);

        return repository.save(patient);
    }

    public List<Patient> getAll() {
        return repository.findAll();
    }

    public List<Patient> getPatientsByDisease(String disease) {
        return repository.findByDisease(disease);
    }

    public Page<Patient> getPatientsPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Patient updatePatient(Long id, PatientDTO patientDTO) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        Doctor doctor = doctorRepository.findById(patientDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + patientDTO.getDoctorId()));

        patient.setName(patientDTO.getName());
        patient.setAge(patientDTO.getAge());
        patient.setDisease(patientDTO.getDisease());
        patient.setPrescription(patientDTO.getPrescription());
        patient.setEmail(patientDTO.getEmail());
        patient.setDoctor(doctor);

        return repository.save(patient);

    }

    public void deletePatient(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        repository.delete(patient);
    }

    public Patient getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found"));
    }

    public List<Patient> getPatientsByName(String name){
        return repository.findByNameContainingIgnoreCase(name);
    }
}

