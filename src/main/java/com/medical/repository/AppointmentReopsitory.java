package com.medical.repository;

import com.medical.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentReopsitory extends JpaRepository<Appointment, Long> {
    @Query("from Appointment a where a.status = false ")
    Page<Appointment> findAllPending(Pageable pageable);

    @Query("from Appointment a where a.status = false and a.doctor.id = :doctor_id ")
    Page<Appointment> findListPendingOfDoctor(Pageable pageable, @Param("doctor_id") Long doctor_id);

    Page<Appointment> findByDoctor_idOrderByIdDesc(Long doctor_id,Pageable pageable);

    Page<Appointment> findByPatient_idOrderByIdDesc(Long patient_id,Pageable pageable);
}
