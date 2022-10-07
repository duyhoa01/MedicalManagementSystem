package com.medical.service;

import com.medical.dtos.DoctorModel;
import com.medical.model.Doctor;
import com.medical.model.User;
import com.medical.repository.DoctorRepository;
import com.medical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;


@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RoleService roleService;

    public Doctor registerDoctor(Doctor doctor, MultipartFile file){

        doctor.getUser().setRole(roleService.getOrCreate("ROLE_DOCTOR"));
        User user= userService.addUser(doctor.getUser());
        if(user==null){
            return  null;
        }
        String url=fileService.StoreFile(file,doctor.getUser().getUsername());
        doctor.setImage(url);
        return doctorRepository.save(doctor);
    }

    public Resource load(String filename) {
        return fileService.load(filename);
    }

    public Page<Doctor> getListDoctor(int pageNumber, int sizePage, String key,String order){
        if(key.equals("")){
            return doctorRepository.findAll(PageRequest.of(pageNumber,sizePage, Sort.by(order).descending()));
        } else {
            return doctorRepository.findDoctorByName(key,PageRequest.of(pageNumber,sizePage, Sort.by(order).descending()));
        }
    }

    public boolean deleteDoctor(Long id){
        Doctor doctor = doctorRepository.findById(id).get();
        if(doctor==null){
            return false;
        }
        User user= userRepository.findById(doctor.getUser().getId()).get();
        doctorRepository.delete(doctor);
        userRepository.delete(user);

        return true;

    }

    public Doctor getDoctorById(Long id){
        try{
            Doctor doctor = doctorRepository.findById(id).get();
            return doctor;
        }catch (Exception e){

        }

        return null;
    }

    public Doctor updateDoctor(Doctor doctor, Long id){
        try{
            Doctor upDoctor=doctorRepository.findById(id).get();
            userService.updateUser(doctor.getUser(),upDoctor.getUser().getId());
            upDoctor.setExperience(doctor.getExperience());
            upDoctor.setImage(doctor.getImage());
            upDoctor.setDescription(doctor.getDescription());

            return doctorRepository.save(upDoctor);
        }catch (Exception e){

        }

        return null;
    }

}
