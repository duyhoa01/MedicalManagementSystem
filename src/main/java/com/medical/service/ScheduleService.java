package com.medical.service;

import com.medical.dtos.SchedulePostDTO;
import com.medical.dtos.ScheduleResponseDTO;
import com.medical.mapper.ScheduleMapper;
import com.medical.model.Patient;
import com.medical.model.Schedule;
import com.medical.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMapper mapper;

    @Autowired
    private PagedResourcesAssembler<Schedule> assembler;

    public ScheduleResponseDTO AddChedule(SchedulePostDTO schedulePostDTO){
        Schedule schedule;
        try {
            schedule = mapper.SchedulePostDTOToScheldule(schedulePostDTO);
        } catch (Exception e) {
            return null;
        }
        if(schedule.getBeginDate().isAfter(schedule.getEndDate()) || schedule.getBeginDate().equals(schedule.getEndDate()) ||checkSchedule(schedule)) {
            return null;
        }

        Schedule newSchedule =  scheduleRepository.save(schedule);

        return mapper.toModel(newSchedule);
    }

    public boolean checkSchedule(Schedule schedule){

        List<Schedule> schedules = scheduleRepository.findByDoctor_User_Username(schedule.getDoctor().getUser().getUsername());
        for (Schedule s:schedules
             ) {
            if(checkMiddle(s.getBeginDate(),s.getEndDate(),schedule.getBeginDate()) || checkMiddle(s.getBeginDate(),s.getEndDate(),schedule.getEndDate())
                || checkMiddle(schedule.getBeginDate(),schedule.getEndDate(),s.getBeginDate())
                || checkMiddle(schedule.getBeginDate(),schedule.getEndDate(),s.getEndDate())
                || checkSame(schedule,s)){
                return true;
            }
        }
        return false;
    }

    public boolean checkUpSchedule(Schedule schedule,Long id){

        List<Schedule> schedules = scheduleRepository.findByDoctor_User_Username(schedule.getDoctor().getUser().getUsername());
        for (Schedule s:schedules
        ) {
            if(s.getId()==id) {
                continue;
            }
            if(checkMiddle(s.getBeginDate(),s.getEndDate(),schedule.getBeginDate()) || checkMiddle(s.getBeginDate(),s.getEndDate(),schedule.getEndDate())
                    || checkMiddle(schedule.getBeginDate(),schedule.getEndDate(),s.getBeginDate())
                    || checkMiddle(schedule.getBeginDate(),schedule.getEndDate(),s.getEndDate())
                    || checkSame(schedule,s)){
                return true;
            }
        }
        return false;
    }

    public boolean checkMiddle(LocalDateTime start, LocalDateTime end, LocalDateTime time){
        if((time.isBefore(end)&& time.isAfter(start))){
            return true;
        }
        return false;
    }

    public boolean checkSame(Schedule schedule, Schedule newSchedule ){
        if(schedule.getBeginDate().equals(newSchedule.getBeginDate()) && schedule.getEndDate().equals(newSchedule.getEndDate())){
            return true;
        }
        return false;
    }

    public PagedModel<ScheduleResponseDTO> getListSchedule(Pageable pageable, LocalDateTime start, LocalDateTime end){
        Page<Schedule> schedules;
        if(start != null && end != null){
            schedules = scheduleRepository.findListSchedule(pageable,start,end);
        } else {
            schedules = scheduleRepository.findAll(pageable);
        }
        return assembler.toModel(schedules,mapper);
    }

    public PagedModel<ScheduleResponseDTO> getListScheduleOfDoctor(Pageable pageable, LocalDateTime start, LocalDateTime end, String username){
        Page<Schedule> schedules;
        if(start != null && end != null){
            schedules = scheduleRepository.findListScheduleOfDoctor(pageable,start,end, username);
        } else {
            schedules = scheduleRepository.findByDoctor_User_Username(username,pageable);
        }
        return assembler.toModel(schedules,mapper);
    }

    public ScheduleResponseDTO updateChedule(SchedulePostDTO schedulePostDTO, Long id){
        Schedule schedule;
        Schedule upSchedule;
        try {
            schedule = mapper.SchedulePostDTOToScheldule(schedulePostDTO);
            upSchedule = scheduleRepository.findById(id).get();
            upSchedule.setBeginDate(schedule.getBeginDate());
            upSchedule.setEndDate(schedule.getEndDate());
            upSchedule.setCost(schedule.getCost());
            if(schedule.getMaxNumber()>upSchedule.getCurrentNumber()) {
                upSchedule.setMaxNumber(schedule.getMaxNumber());
            }
        } catch (Exception e) {
            return null;
        }

        if(upSchedule.getBeginDate().isAfter(upSchedule.getEndDate()) || upSchedule.getBeginDate().equals(upSchedule.getEndDate()) ||checkUpSchedule(upSchedule,id)) {
            return null;
        }

        Schedule newSchedule =  scheduleRepository.save(upSchedule);

        return mapper.toModel(newSchedule);
    }

    public ScheduleResponseDTO getSchedule(Long id){
        try{
            return mapper.toModel(scheduleRepository.findById(id).get());
        } catch (Exception e){

        }
        return null;
    }
}
