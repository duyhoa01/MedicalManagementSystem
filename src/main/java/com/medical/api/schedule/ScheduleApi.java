package com.medical.api.schedule;

import com.medical.dtos.MessageResponse;
import com.medical.dtos.SchedulePostDTO;
import com.medical.dtos.ScheduleResponseDTO;
import com.medical.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleApi {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("")
    public ResponseEntity<?> addSchedule(@RequestBody @Valid SchedulePostDTO schedulePostDTO){
        ScheduleResponseDTO scheduleResponseDTO = scheduleService.AddChedule(schedulePostDTO);
        if(scheduleResponseDTO  == null){
            return new ResponseEntity<>(new MessageResponse("tạo kế hoạch khám bệnh thất bại"),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(scheduleResponseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListSchedule(Pageable pageable,
                                             @RequestParam(required = false, defaultValue = "") String start,
                                             @RequestParam(required = false, defaultValue = "") String end){
        LocalDateTime startdate = start.equals("")? null:LocalDateTime.parse(start);
        LocalDateTime enddate = end.equals("")? null:LocalDateTime.parse(end);
        return new ResponseEntity<>(scheduleService.getListSchedule(pageable,startdate,enddate),HttpStatus.OK);
    }

    @GetMapping("/doctor/{username}")
    public ResponseEntity<?> getListScheduleOfDoctor(Pageable pageable,
                                             @RequestParam(required = false, defaultValue = "") String start,
                                             @RequestParam(required = false, defaultValue = "") String end,
                                                     @PathVariable String username){
        LocalDateTime startdate = start.equals("")? null:LocalDateTime.parse(start);
        LocalDateTime enddate = end.equals("")? null:LocalDateTime.parse(end);
        return new ResponseEntity<>(scheduleService.getListScheduleOfDoctor(pageable,startdate,enddate,username),HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSchedule(@RequestBody @Valid SchedulePostDTO schedulePostDTO,
                                            @PathVariable Long id){
        ScheduleResponseDTO scheduleResponseDTO = scheduleService.updateChedule(schedulePostDTO,id);
        if(scheduleResponseDTO  == null){
            return new ResponseEntity<>(new MessageResponse("cập nhật kế hoạch khám bệnh thất bại"),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(scheduleResponseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id){
        ScheduleResponseDTO schedule = scheduleService.getSchedule(id);
        if(schedule == null){
            return new ResponseEntity<>(new MessageResponse("không tìm thấy kế hoạch"),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(schedule,HttpStatus.OK);
        }
    }

}
