package com.medical.dtos;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentPostDTO {

    @NotNull(message = "thiếu trường doctor_id")
    private Long doctor_id;

    @NotNull(message = "thiếu trường patient_id")
    private Long patient_id;

    @NotNull(message = "thiếu trường date")
    private LocalDateTime date;

    @NotNull(message = "thiếu trường symptoms")
    private String symptoms;

}
