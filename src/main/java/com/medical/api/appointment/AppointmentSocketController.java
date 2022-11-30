package com.medical.api.appointment;


import com.medical.dtos.MessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class AppointmentSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/private-appointment")
    public String recMessage(@Payload String doctor){
        System.out.println(doctor);
        simpMessagingTemplate.convertAndSendToUser(doctor,"/appointment",doctor);
        return doctor;
    }

    @MessageMapping("/private-accept-appointment")
    public String recAppceptAppointment(@Payload String patient){
        simpMessagingTemplate.convertAndSendToUser(patient,"/patient-appointment",patient);
        return patient;
    }
}
