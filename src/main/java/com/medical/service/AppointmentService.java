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

        appointment.setDate(LocalDateTime.now());

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
            if(doctor1.getId() != id){
                throw new AccessDeniedException("Access is denied");
            }
        }

        appointment.get().setStatus(true);

        return mapper.toModel(appointmentReopsitory.save(appointment.get()));

    }
}
