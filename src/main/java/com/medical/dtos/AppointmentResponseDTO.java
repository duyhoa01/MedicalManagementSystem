package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@JsonRootName(value = "appoinment")
@Relation(collectionRelation = "appoinments")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AppointmentResponseDTO extends RepresentationModel<AppointmentResponseDTO> {

    private Long id;

    private String patient;

    private Long patient_id;

    private String doctor;

    private Long doctor_id;

    private Double cost;

    private LocalDateTime date;

    private String symptoms;

    private Boolean status;

}
