package com.medical.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class SchedulePostDTO {

    @NotNull(message = "thiếu số lượng tối đa người bệnh")
    private int maxNumber;

    @NotNull(message = "thiếu thời gian kết thúc")
    private LocalDateTime endDate;

    @NotNull(message = "thiếu thời gian bắt đầu")
    private LocalDateTime beginDate;

    @NotNull(message = "thiếu chi phí")
    private Double cost;

    @NotEmpty(message = "thiếu bác sĩ")
    private String doctor;

}
