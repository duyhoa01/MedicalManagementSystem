package com.medical.repository;

import com.medical.model.Patient;
import com.medical.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Specialty s " +
            "WHERE s.name = ?1"
    )
    Boolean selectExistsName(String name);

    @Query("from Specialty s where s.name LIKE CONCAT('%',:name,'%')")
    List<Specialty> findListSpecialty(@Param("name") String name);
}
