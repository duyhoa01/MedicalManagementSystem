package com.medical.dtos.modelAssembler;

import com.medical.api.doctor.DoctorApi;
import com.medical.dtos.DoctorModel;
import com.medical.model.Doctor;
import com.medical.model.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class DoctorModelAssembler implements RepresentationModelAssembler<Doctor, DoctorModel> {

    UserModelAssembler userModelAssembler=new UserModelAssembler();

    @Override
    public DoctorModel toModel(Doctor entity) {
        DoctorModel doctorModel=new DoctorModel();
        doctorModel.setId(entity.getId());
        doctorModel.setDescription(entity.getDescription());
        doctorModel.setImage(entity.getImage());
        doctorModel.setRate(entity.getRate());
        doctorModel.setExperience(entity.getExperience());

        doctorModel.setUser(userModelAssembler.toModel(entity.getUser()));

        doctorModel.add(linkTo(methodOn(DoctorApi.class).getDoctor(doctorModel.getId())).withSelfRel());

//        String fileDownloadUri= ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/auth/files/")
//                .path(entity.getImage().hashCode())
//                .toUriString();

        return doctorModel;
    }

    public Doctor convertToDoctor(DoctorModel doctorModel) {
        Doctor doctor=new Doctor();
        User user = userModelAssembler.convertToUser(doctorModel.getUser());
        doctor.setImage(doctorModel.getImage());
        doctor.setExperience(doctorModel.getExperience());
        doctor.setDescription(doctorModel.getDescription());
        doctor.setUser(user);

        return doctor;
    }



}
