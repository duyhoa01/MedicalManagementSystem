package com.medical.service;

import com.medical.dtos.AppointmentPostDTO;
import com.medical.dtos.AppointmentResponseDTO;
import com.medical.mapper.AppointmentMapper;
import com.medical.model.Appointment;
import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.User;
import com.medical.repository.AppointmentReopsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentReopsitory appointmentReopsitory;

    @Autowired
    private AppointmentMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PagedResourcesAssembler<Appointment> assembler;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public AppointmentResponseDTO addAppointment(AppointmentPostDTO appointmentPostDTO){

        Appointment appointment =  mapper.DTOToAppointment(appointmentPostDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))){
            String currentPrincipalName = authentication.getName();
            Patient patient= ((User)userService.loadUserByUsername(currentPrincipalName)).getPatient();
            if(patient.getId() != appointment.getPatient().getId()){
                throw new AccessDeniedException("Access is denied");
            }
            appointment.setStatus(false);
        } else {
            appointment.setStatus(true);
        }

//        System.out.println(LocalDateTime.now());
//        appointment.setDate(LocalDateTime.now());

        return mapper.toModel(appointmentReopsitory.save(appointment));

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public AppointmentResponseDTO enableAppointment(Long id){
        Optional<Appointment> appointment = appointmentReopsitory.findById(id);
        if(appointment.isEmpty()){
            throw new NoSuchElementException("id appointment không tồn tại");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Doctor doctor1= ((User)userService.loadUserByUsername(currentPrincipalName)).getDoctor();
            if(doctor1.getId() != appointment.get().getDoctor().getId()){
                throw new AccessDeniedException("Access is denied");
            }
        }

        appointment.get().setStatus(true);

        return mapper.toModel(appointmentReopsitory.save(appointment.get()));

    }

    @PreAuthorize("hasRole('ADMIN')")
    public PagedModel<AppointmentResponseDTO> getListAppointmentPending(Pageable pageable){
            return assembler.toModel(appointmentReopsitory.findAllPending(pageable), mapper);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PagedModel<AppointmentResponseDTO> getListAppointment(Pageable pageable){
        return assembler.toModel(appointmentReopsitory.findAll(pageable), mapper);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public PagedModel<AppointmentResponseDTO> getListAppointmentPendingOfDoctor(Pageable pageable, Long doctor_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Doctor doctor1= ((User)userService.loadUserByUsername(currentPrincipalName)).getDoctor();
            if(doctor1.getId() != doctor_id){
                throw new AccessDeniedException("Access is denied");
            }
        }
        return assembler.toModel(appointmentReopsitory.findListPendingOfDoctor(pageable,doctor_id), mapper);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public PagedModel<AppointmentResponseDTO> getListAppointmentOfDoctor(Pageable pageable, Long doctor_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Doctor doctor1= ((User)userService.loadUserByUsername(currentPrincipalName)).getDoctor();
            if(doctor1.getId() != doctor_id){
                throw new AccessDeniedException("Access is denied");
            }
        }
        return assembler.toModel(appointmentReopsitory.findByDoctor_idOrderByIdDesc(doctor_id,pageable), mapper);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public PagedModel<AppointmentResponseDTO> getListAppointmentOfPatient(Pageable pageable, Long payient_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))){
            String currentPrincipalName = authentication.getName();
            System.out.println("name"+currentPrincipalName);
            Patient patient= ((User)userService.loadUserByUsername(currentPrincipalName)).getPatient();
            if(patient.getId() != payient_id){
                throw new AccessDeniedException("Access is denied");
            }
        }

        return assembler.toModel(appointmentReopsitory.findByPatient_idOrderByIdDesc(payient_id,pageable), mapper);
    }
}
