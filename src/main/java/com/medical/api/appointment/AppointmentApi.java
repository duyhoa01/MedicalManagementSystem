package com.medical.api.appointment;

import com.medical.dtos.AppointmentPostDTO;
import com.medical.dtos.MessageResponse;
import com.medical.model.Appointment;
import com.medical.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController()
@RequestMapping("/api/appointments")
public class AppointmentApi {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("")
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentPostDTO appointmentPostDTO){
        try{
            return new ResponseEntity<>(appointmentService.addAppointment(appointmentPostDTO),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }
    };

    @GetMapping("/{id}/enable")
    public ResponseEntity<?> enableAppointment(@PathVariable Long id){
        try{
            return new ResponseEntity<>(appointmentService.enableAppointment(id),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getListAppointmentPending(Pageable pageable){
        try{
            return new ResponseEntity<>(appointmentService.getListAppointmentPending(pageable),HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListAppointment(Pageable pageable){
        try{
            return new ResponseEntity<>(appointmentService.getListAppointment(pageable),HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(appointmentService.getAppointmentById(id),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

}
