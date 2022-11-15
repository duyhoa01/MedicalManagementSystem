package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@JsonRootName(value = "schedule")
@Relation(collectionRelation = "schedules")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ScheduleResponseDTO extends RepresentationModel<ScheduleResponseDTO> {

    private Long id;

    private int currentNumber;

    private int maxNumber;

    private LocalDateTime endDate;

    private LocalDateTime beginDate;

    private Double cost;

    private DoctorResponseDTO doctor;

    private List<AppoinmentResponseDTO> appoinments;
}
