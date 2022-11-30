package com.medical.api.chat;


import com.medical.dtos.MessagePostDTO;
import com.medical.dtos.MessageResponseDTO;
import com.medical.mapper.MessageMapper;
import com.medical.model.Message;
import com.medical.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper mapper;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public MessageResponseDTO recMessage(@Payload MessageResponseDTO message){
//            Message message1= mapper.DtoToEntity(message);
//            MessageResponseDTO messageResponse = mapper.toModel(message1);
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(),"/private",message);
            return message;

    }
}
