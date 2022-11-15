package com.medical.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Data
public class ChangePassDTO {

    @NotEmpty(message = "thiếu username")
    private String username;

    @NotEmpty(message = "thiếu password mới")
    private String password;

    @NotEmpty(message = "thiếu password cũ")
    private String oldPassword;
}
