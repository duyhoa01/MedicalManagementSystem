package com.medical.api.chat;

import com.medical.dtos.MessagePostDTO;
import com.medical.dtos.MessageResponse;
import com.medical.dtos.MessageResponseDTO;
import com.medical.model.Message;
import com.medical.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/messages")
public class MessageApi {
    @Autowired
    private MessageService messageService;

    @PostMapping("")
    public ResponseEntity<?> addMessage(@RequestBody MessagePostDTO message){
        try{
            MessageResponseDTO messageResponse = messageService.addMessage(message);
            return new ResponseEntity<>(messageResponse,HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListUserandUser(@RequestParam(name = "sender_id") Long sender_id,
                                                @RequestParam(name = "receiver_id") Long receiver_id,
                                                Pageable pageable){
        return new ResponseEntity<>(messageService.getListMessage(sender_id,receiver_id,pageable), HttpStatus.OK);
    }
}
