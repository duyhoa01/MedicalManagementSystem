package com.medical.repository;

import com.medical.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Page<Doctor> findByUser_LastNameContaining(String name, Pageable pageable);

    Page<Doctor> findBySpecialty_Id(Long id, Pageable pageable);

    @Query("select d from Doctor d JOIN d.user  u  where CONCAT(u.firstName,' ',u.lastName) LIKE CONCAT('%',:name,'%')")
    Page<Doctor> findDoctorByName(@Param("name") String name, Pageable pageable);

    @Query("select d from Doctor d JOIN d.user  u  where CONCAT(u.firstName,' ',u.lastName) LIKE CONCAT('%',:name,'%') AND d.specialty.id = :specialty")
    Page<Doctor> findDoctorByNameAndSpecialty(@Param("name") String name, Pageable pageable, @Param("specialty") Long specialty);
}
