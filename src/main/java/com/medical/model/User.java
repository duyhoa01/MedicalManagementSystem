package com.medical.model;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private Boolean sex;

    private int age;

    @Column(name="phone_number")
    private String phoneNumber;

    private Boolean status;

    @ManyToOne
    private Role role;
}
