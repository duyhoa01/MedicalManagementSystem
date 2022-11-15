package com.medical.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
public class UserPostDTO {
    private String username;

    private String password;

    @NotEmpty(message = "thiếu firstname")
    private String firstName;

    @NotEmpty(message = "thiếu lastname")
    private String lastName;

    @NotNull(message = "thiếu email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "thiếu giới tính")
    private Boolean sex;

    @NotNull(message = "thiếu tuổi")
    private int age;

    @NotEmpty(message = "thiếu số điện thoại")
    private String phoneNumber;

    private Boolean status;
}
