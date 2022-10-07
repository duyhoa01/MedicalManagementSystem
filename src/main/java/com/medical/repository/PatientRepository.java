package com.medical.repository;

import com.medical.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    @Query("select p from Patient p JOIN p.user  u  where u.lastName LIKE CONCAT('%',:name,'%') or u.firstName LIKE CONCAT('%',:name,'%')")
    Page<Patient> findListPatient(@Param("name") String name, Pageable pageable);
}
