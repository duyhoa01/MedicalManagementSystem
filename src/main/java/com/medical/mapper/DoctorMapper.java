package com.medical.mapper;

import com.medical.api.doctor.DoctorApi;
import com.medical.dtos.*;
import com.medical.model.Doctor;
import com.medical.model.Specialty;
import com.medical.model.User;
import com.medical.repository.SpecialtyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class DoctorMapper implements RepresentationModelAssembler<Doctor, DoctorResponseDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public DoctorResponseDTO toModel(Doctor entity) {
        DoctorResponseDTO doctorDto=modelMapper.map(entity, DoctorResponseDTO.class);
        doctorDto.setUser(modelMapper.map(entity.getUser(), UserResponeDTO.class));
        doctorDto.setSpecialty(entity.getSpecialty().getName());
        doctorDto.setSpecialty_id(entity.getSpecialty().getId());

        doctorDto.add(linkTo(methodOn(DoctorApi.class).getDoctor(doctorDto.getId())).withSelfRel());

        return doctorDto;
    }

    public Doctor DoctorPostDTOToDoctor(DoctorPostDTO doctorPostDTO) throws Exception {
        Doctor doctor= modelMapper.map(doctorPostDTO,Doctor.class);
        doctor.setUser(modelMapper.map(doctorPostDTO.getUser(), User.class));
        Optional<Specialty> specialty = specialtyRepository.findById(doctorPostDTO.getSpecialty());
        if(specialty.isEmpty()){
            throw new Exception("id specialty không tồn tại");
        }
        doctor.setSpecialty(specialty.get());

        return  doctor;
    }
}
