package com.medical.dtos.modelAssembler;

import com.medical.api.doctor.DoctorApi;
import com.medical.api.patient.PatientApi;
import com.medical.dtos.DoctorModel;
import com.medical.dtos.PatientModel;
import com.medical.model.Doctor;
import com.medical.model.Patient;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PatientModelAssembler implements RepresentationModelAssembler<Patient, PatientModel> {

    UserModelAssembler userModelAssembler=new UserModelAssembler();

    @Override
    public PatientModel toModel(Patient entity) {
        PatientModel patientModel=new PatientModel();
        patientModel.setId(entity.getId());
        patientModel.setJob(entity.getJob());
        patientModel.setUser(userModelAssembler.toModel(entity.getUser()));

        patientModel.add(linkTo(methodOn(PatientApi.class).getPatient(patientModel.getId())).withSelfRel());

        return patientModel;
    }
}
