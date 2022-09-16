package com.medical.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class)
    private User user;

    private String job;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
}
