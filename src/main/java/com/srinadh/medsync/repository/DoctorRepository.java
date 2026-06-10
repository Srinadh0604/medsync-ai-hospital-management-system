package com.srinadh.medsync.repository;

import com.srinadh.medsync.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {


}