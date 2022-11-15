package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@JsonRootName(value = "user")
@Relation(collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserResponeDTO extends RepresentationModel<UserResponeDTO> {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean sex;

    private int age;

    private String phoneNumber;

    private Boolean status;

    private String image;

}
