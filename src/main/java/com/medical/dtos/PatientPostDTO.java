package com.medical.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
public class PatientPostDTO {
    @Valid
    private UserPostDTO user;

    @NotEmpty
    private String job;

}
