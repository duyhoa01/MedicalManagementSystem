package com.medical.mapper;

import com.medical.dtos.AppointmentPostDTO;
import com.medical.dtos.AppointmentResponseDTO;
import com.medical.dtos.DoctorPostDTO;
import com.medical.dtos.DoctorResponseDTO;
import com.medical.model.Appointment;
import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.repository.AppointmentReopsitory;
import com.medical.repository.DoctorRepository;
import com.medical.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.NoSuchElementException;
import java.util.Optional;

public class AppointmentMapper implements RepresentationModelAssembler<Appointment, AppointmentResponseDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Appointment DTOToAppointment(AppointmentPostDTO appointmentPostDTO){
        Appointment appointment =mapper.map(appointmentPostDTO,Appointment.class);
        Optional<Doctor> doctor = doctorRepository.findById(appointmentPostDTO.getDoctor_id());
        if(doctor.isEmpty()){
            throw new NoSuchElementException("doctor_id không tồn tại");
        }
        Optional<Patient> patient = patientRepository.findById(appointmentPostDTO.getPatient_id());
        if(patient.isEmpty()){
            throw new NoSuchElementException("patient_id không tồn tại");
        }
        appointment.setDoctor(doctor.get());
        appointment.setPatient(patient.get());
        return appointment;
    }

    @Override
    public AppointmentResponseDTO toModel(Appointment entity) {

        AppointmentResponseDTO appointmentResponseDTO = mapper.map(entity, AppointmentResponseDTO.class);
        appointmentResponseDTO.setDoctor_id(entity.getDoctor().getUser().getId());
        appointmentResponseDTO.setDoctor(entity.getDoctor().getUser().getFirstName()+ " " +entity.getDoctor().getUser().getLastName());
        appointmentResponseDTO.setPatient(entity.getPatient().getUser().getFirstName()+ " " +entity.getPatient().getUser().getLastName());
        appointmentResponseDTO.setPatient_id(entity.getPatient().getUser().getId());

        return appointmentResponseDTO;
    }
}
