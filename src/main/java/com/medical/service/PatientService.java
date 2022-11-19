package com.medical.service;

import com.medical.dtos.PatientResponeDTO;
import com.medical.dtos.PatientPostDTO;
import com.medical.mapper.PatientMapper;
import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.User;
import com.medical.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.SendFailedException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PagedResourcesAssembler<Patient> assembler;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private FileService fileService;

//    @PreAuthorize("hasRole('ADMIN')")
    public PatientResponeDTO addPatient(PatientPostDTO patientPostDTO, MultipartFile image) throws SendFailedException {
        Patient patient = patientMapper.PatientPostDTOToPatient(patientPostDTO);
        String url ;
        if(image == null){
            if(patient.getUser().getSex()==true){
                url="https://res.cloudinary.com/drotiisfy/image/upload/v1665540808/profiles/male_default_avatar.jng_tgqrqf.jpg";
            } else {
                url="https://res.cloudinary.com/drotiisfy/image/upload/v1665540809/profiles/female_defaule_avatar_ezuxcv.jpg";
            }
        } else {
            url = fileService.uploadFile(image);
        }
        patient.getUser().setRole(roleService.getOrCreate("ROLE_PATIENT"));
        patient.getUser().setImage(url);
        User user= userService.addUser(patient.getUser());
//        if(user==null){
//            throw new IllegalStateException("không tìm thấy bệnh nhân");
//        }
        Patient newPatient = patientRepository.save(patient);

        return patientMapper.toModel(newPatient);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public PatientResponeDTO updatePatient(PatientPostDTO patientPostDTO, MultipartFile image, Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Patient patient= ((User)userService.loadUserByUsername(currentPrincipalName)).getPatient();
            if(patient.getId() != id){
                throw new AccessDeniedException("Access is denied");
            }
        }

        Patient patient = patientMapper.PatientPostDTOToPatient(patientPostDTO);
        Optional<Patient> upPatient = patientRepository.findById(id);
        if(upPatient.isEmpty()){
            throw  new NoSuchElementException("id patient không tồn tại");
        }

        userService.updateUser(patient.getUser(),upPatient.get().getUser().getId(),image);
        upPatient.get().setJob(patient.getJob());

        return patientMapper.toModel(patientRepository.save(upPatient.get()));

    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public PatientResponeDTO getPatientById(Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Patient patient= ((User)userService.loadUserByUsername(currentPrincipalName)).getPatient();
            if(patient.getId() != id){
                throw new AccessDeniedException("Access is denied");
            }
        }
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            throw new NoSuchElementException("id patient không tồn tại");
        }
        return patientMapper.toModel(patient.get());

    }

    @PreAuthorize("hasRole('ADMIN')")
    public PagedModel<PatientResponeDTO> getListPatient(Pageable pageable, String key){

        try{
            if( key.equals("")){
                return assembler.toModel(patientRepository.findAll(pageable),patientMapper);
            } else {
                return assembler.toModel(patientRepository.findListPatient(key,pageable),patientMapper);
            }
        } catch (Exception e){

        }
        return null;

    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatient(Long id){

        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            throw  new NoSuchElementException("id patient không tồn tại");
        }
        patientRepository.delete(patient.get());
        userService.deleteUser(patient.get().getUser().getId());
    }

}
