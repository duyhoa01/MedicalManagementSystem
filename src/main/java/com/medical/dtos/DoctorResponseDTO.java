package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@JsonRootName(value = "doctor")
@Relation(collectionRelation = "doctors")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DoctorResponseDTO extends RepresentationModel<DoctorResponseDTO> {
    private Long id;

    private UserResponeDTO user;

    private float experience;

    private float rate;

    private String description;

    private String specialty;

}
