package com.medical.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialtyDTO {
    private Long id;
    private String name;
    private String description;
    private int sumdoctor;
}
