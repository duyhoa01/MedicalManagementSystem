package com.medical.repository;

import com.medical.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentReopsitory extends JpaRepository<Appointment, Long> {

}
