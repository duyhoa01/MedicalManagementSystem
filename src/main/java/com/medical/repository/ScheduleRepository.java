package com.medical.repository;

import com.medical.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctor_User_Username(String username);

    Page<Schedule> findByDoctor_User_Username(String username, Pageable pageable);

    @Query("FROM Schedule s where s.beginDate >= :start and s.endDate <= :end")
    Page<Schedule> findListSchedule(Pageable pageable, @Param("start") LocalDateTime start,@Param("end") LocalDateTime end);

    @Query("FROM Schedule s where s.beginDate >= :start and s.endDate <= :end and s.doctor.user.username= :username")
    Page<Schedule> findListScheduleOfDoctor(Pageable pageable, @Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end,
                                                            @Param("username") String username);
}
