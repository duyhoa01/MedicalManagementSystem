package com.medical.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentNumber;

    private int maxNumber;

    private LocalDateTime endDate;

    private LocalDateTime beginDate;

    private Double cost;

    @ManyToOne
    private Doctor doctor;

    @OneToMany(mappedBy = "schedule")
    private List<Appointment> appointments;

}
