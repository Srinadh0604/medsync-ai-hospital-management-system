package com.srinadh.medsync.repository;

import com.srinadh.medsync.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}