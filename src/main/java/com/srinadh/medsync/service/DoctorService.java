package com.srinadh.medsync.service;

import com.srinadh.medsync.entity.Doctor;
import com.srinadh.medsync.exception.DuplicateDoctorException;
import com.srinadh.medsync.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (repository.existsByEmail(
                doctor.getEmail())) {

            throw new DuplicateDoctorException(
                    "Doctor email already exists");
        }
        return repository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }
}

