package com.medical.service;

import com.medical.dtos.SpecialtyDTO;
import com.medical.model.Specialty;
import com.medical.repository.SpecialtyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private ModelMapper mapper;

    public SpecialtyDTO AddSpecialty(SpecialtyDTO specialtyDTO){
        Specialty specialty = mapper.map(specialtyDTO,Specialty.class);

        boolean ok = specialtyRepository.selectExistsName(specialty.getName());
        if(ok) return null;
        Specialty newP= specialtyRepository.save(specialty);

        return mapper.map(newP,SpecialtyDTO.class);
    }

    public List<SpecialtyDTO> getAllSpecialty(String key){
        if(key.equals("")){
            List<Specialty>  specialties =  specialtyRepository.findAll();
            return specialties.stream().map(s -> {
                        SpecialtyDTO specialtyDTO= mapper.map(s,SpecialtyDTO.class);
                        specialtyDTO.setSumdoctor(s.getDoctors().size());
                        return specialtyDTO;
                    }
            ).collect(Collectors.toList());
        } else {
            List<Specialty> specialties = specialtyRepository.findListSpecialty(key);
            return specialties.stream().map(s -> {
                        SpecialtyDTO specialtyDTO= mapper.map(s,SpecialtyDTO.class);
                        specialtyDTO.setSumdoctor(s.getDoctors().size());
                        return specialtyDTO;
                    }
            ).collect(Collectors.toList());
        }
    }

    public SpecialtyDTO getSpecialtyById(Long id){
        try{
            Specialty specialty= specialtyRepository.findById(id).get();
            SpecialtyDTO specialtyDTO = mapper.map(specialty,SpecialtyDTO.class);
            specialtyDTO.setSumdoctor(specialty.getDoctors().size());

            return specialtyDTO;
        } catch (Exception e){

        }
        return null;
    }

    public SpecialtyDTO updateSpecialty(Long id,SpecialtyDTO specialtyDTO){
        try {
            Specialty specialty= specialtyRepository.findById(id).get();
            specialty.setName(specialtyDTO.getName());
            specialty.setDescription(specialtyDTO.getName());

            SpecialtyDTO upSepDTO =  mapper.map(specialtyRepository.save(specialty),SpecialtyDTO.class);
            upSepDTO.setSumdoctor(specialty.getDoctors().size());

            return upSepDTO;
        } catch (Exception e){

        }
        return null;
    }
}
