package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.medical.model.User;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.web.multipart.MultipartFile;

@JsonRootName(value = "doctor")
@Relation(collectionRelation = "doctors")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorModel extends RepresentationModel<DoctorModel> {
    private Long id;

    private UserModel user;

    private String image;

    private float experience;

    private float rate;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DoctorModel{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", image='" + image + '\'' +
                ", experience=" + experience +
                ", rate=" + rate +
                ", description='" + description + '\'' +
                '}';
    }
}
