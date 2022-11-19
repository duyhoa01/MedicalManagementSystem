package com.medical.service;

import com.medical.dtos.DoctorPostDTO;
import com.medical.dtos.DoctorResponseDTO;
import com.medical.mapper.DoctorMapper;
import com.medical.model.Doctor;
import com.medical.model.Specialty;
import com.medical.model.User;
import com.medical.repository.DoctorRepository;
import com.medical.repository.SpecialtyRepository;
import com.medical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private DoctorMapper mapper;

    @Autowired
    private PagedResourcesAssembler<Doctor> assembler;

    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponseDTO addDoctor(DoctorPostDTO doctorPostDTO, MultipartFile image) throws SendFailedException {

        Doctor doctor = null;
        try {
            doctor = mapper.DoctorPostDTOToDoctor(doctorPostDTO);
        } catch (Exception e) {
            return null;
        }

        doctor.getUser().setRole(roleService.getOrCreate("ROLE_DOCTOR"));
        String url;
        if(image == null){
            if(doctor.getUser().getSex()==true){
                url="https://res.cloudinary.com/drotiisfy/image/upload/v1665540808/profiles/male_default_avatar.jng_tgqrqf.jpg";
            } else {
                url="https://res.cloudinary.com/drotiisfy/image/upload/v1665540809/profiles/female_defaule_avatar_ezuxcv.jpg";
            }
        } else {
            url = fileService.uploadFile(image);
        }
        doctor.getUser().setStatus(true);
        doctor.setRate(0);
        doctor.getUser().setImage(url);
        User user= userService.addUser(doctor.getUser());
        if(user==null){
            return  null;
        }
        return mapper.toModel(doctorRepository.save(doctor));
    }

    public Resource load(String filename) {
        return fileService.load(filename);
    }

    public PagedModel<DoctorResponseDTO> getListDoctor(Pageable pageable, String key, Long specialty){
        try{
            if(key.equals("")){
                if(specialty == 0){
                    return assembler.toModel(doctorRepository.findAll(pageable),mapper);
                } else {
                    return assembler.toModel(doctorRepository.findBySpecialty_Id(specialty,pageable),mapper);
                }

            } else {
                if(specialty == 0){
                    return assembler.toModel(doctorRepository.findDoctorByName(key,pageable),mapper);
                } else {
                    return assembler.toModel(doctorRepository.findDoctorByNameAndSpecialty(key,pageable,specialty), mapper);
                }

            }
        } catch (Exception e){

        }
        return  null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDoctor(Long id){

        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isEmpty()){
            throw  new NoSuchElementException("id doctor không tồn tại");
        }
        doctorRepository.delete(doctor.get());
        userService.deleteUser(doctor.get().getUser().getId());
    }

    public DoctorResponseDTO getDoctorById(Long id){
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isEmpty()){
            throw new NoSuchElementException("id doctor không tồn tại");
        }
        return mapper.toModel(doctor.get());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public DoctorResponseDTO updateDoctor(DoctorPostDTO doctorPostDTO,MultipartFile image,Long id) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Doctor doctor1= ((User)userService.loadUserByUsername(currentPrincipalName)).getDoctor();
            if(doctor1.getId() != id){
                throw new AccessDeniedException("Access is denied");
            }
        }

        Doctor doctor = mapper.DoctorPostDTOToDoctor(doctorPostDTO);
        Optional<Doctor> upDoctor = doctorRepository.findById(id);
        if(upDoctor.isEmpty()){
            throw  new NoSuchElementException("id doctor không tồn tại");
        }

        userService.updateUser(doctor.getUser(),upDoctor.get().getUser().getId(),image);
        upDoctor.get().setSpecialty(specialtyRepository.findById(doctorPostDTO.getSpecialty()).get());
        upDoctor.get().setExperience(doctor.getExperience());
        upDoctor.get().setDescription(doctor.getDescription());

        return mapper.toModel(doctorRepository.save(upDoctor.get()));
    }

}
