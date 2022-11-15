package com.medical.mapper;

import com.medical.dtos.AppoinmentResponseDTO;
import com.medical.dtos.DoctorResponseDTO;
import com.medical.dtos.SchedulePostDTO;
import com.medical.dtos.ScheduleResponseDTO;
import com.medical.model.Doctor;
import com.medical.model.Schedule;
import com.medical.model.User;
import com.medical.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.stream.Collectors;

public class ScheduleMapper implements RepresentationModelAssembler<Schedule, ScheduleResponseDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ScheduleResponseDTO toModel(Schedule entity) {
        ScheduleResponseDTO scheduleResponseDTO = mapper.map(entity,ScheduleResponseDTO.class);
        scheduleResponseDTO.setDoctor(mapper.map(entity.getDoctor(),DoctorResponseDTO.class));
        try{
            entity.getAppointments().stream().map(a -> {
                AppoinmentResponseDTO appoinment= mapper.map(a,AppoinmentResponseDTO.class);
                appoinment.setPatient(a.getPatient().getUser().getUsername());
                appoinment.setDoctor(a.getSchedule().getDoctor().getUser().getUsername());
                return appoinment;
            }).collect(Collectors.toList());
        } catch (Exception e){

        }

        return scheduleResponseDTO;
    }

    public Schedule SchedulePostDTOToScheldule(SchedulePostDTO schedulePostDTO) throws Exception{
        Schedule  schedule = mapper.map(schedulePostDTO,Schedule.class);
        Doctor doctor=userRepository.findByUsername(schedulePostDTO.getDoctor()).getDoctor();
        schedule.setDoctor(doctor);
        return schedule;
    }
}
