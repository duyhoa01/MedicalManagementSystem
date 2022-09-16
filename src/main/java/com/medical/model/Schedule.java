package com.medical.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentNumber;

    private int maxNumber;

    private long timeSlot;

    private Date date;

    private Double cost;

    @ManyToOne
    private Doctor doctor;

}
