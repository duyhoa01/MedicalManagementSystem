package com.medical.mapper;

import com.medical.api.patient.PatientApi;
import com.medical.dtos.PatientResponeDTO;
import com.medical.dtos.PatientPostDTO;
import com.medical.dtos.UserResponeDTO;
import com.medical.model.Patient;
import com.medical.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PatientMapper implements RepresentationModelAssembler<Patient, PatientResponeDTO> {
    @Autowired
    private ModelMapper modelMapper;

    public Patient PatientPostDTOToPatient(PatientPostDTO patientPostDTO){
        Patient patient= modelMapper.map(patientPostDTO,Patient.class);
        patient.setUser(modelMapper.map(patientPostDTO.getUser(), User.class));

        return  patient;
    }

    @Override
    public PatientResponeDTO toModel(Patient entity) {
        PatientResponeDTO patientModel=modelMapper.map(entity, PatientResponeDTO.class);
        patientModel.setUser(modelMapper.map(entity.getUser(), UserResponeDTO.class));

        patientModel.add(linkTo(methodOn(PatientApi.class).getPatient(patientModel.getUser().getId())).withSelfRel());

        return patientModel;
    }
}
