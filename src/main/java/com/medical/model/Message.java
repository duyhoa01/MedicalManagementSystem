package com.medical.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String image;

    private LocalDateTime date;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

}
