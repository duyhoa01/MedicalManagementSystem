package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@JsonRootName(value = "patient")
@Relation(collectionRelation = "patients")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PatientResponeDTO extends RepresentationModel<PatientResponeDTO> {
    private Long id;

    private String job;

    private UserResponeDTO user;

}
