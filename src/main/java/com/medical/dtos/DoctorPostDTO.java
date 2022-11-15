package com.medical.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
public class DoctorPostDTO {

    @Valid
    private UserPostDTO user;

    @NotNull(message = "thiếu trường kinh nghiệm")
    private float experience;

    @NotEmpty(message = "thiếu mô tả")
    private String description;

    @NotNull(message = "thiếu chuyên khoa")
    private Long specialty;

}
