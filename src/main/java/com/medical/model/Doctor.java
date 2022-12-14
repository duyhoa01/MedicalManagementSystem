package com.medical.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class)
    private User user;

    private float experience;

    private float rate;

    private String description;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    @ManyToOne
    private Specialty specialty;
}
