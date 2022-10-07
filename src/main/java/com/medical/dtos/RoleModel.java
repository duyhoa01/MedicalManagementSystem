package com.medical.dtos;

import com.medical.model.Role;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

public class RoleModel extends RepresentationModel<RoleModel> {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
