package com.medical.repository;

import com.medical.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Page<Doctor> findByUser_LastNameContaining(String name, Pageable pageable);

    @Query("select d from Doctor d JOIN d.user  u  where u.lastName LIKE CONCAT('%',:name,'%') or u.firstName LIKE CONCAT('%',:name,'%')")
    Page<Doctor> findDoctorByName(@Param("name") String name, Pageable pageable);
}
