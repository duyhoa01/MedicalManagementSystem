package com.medical.service;

import com.medical.dtos.MessagePostDTO;
import com.medical.dtos.MessageResponseDTO;
import com.medical.mapper.MessageMapper;
import com.medical.model.Message;
import com.medical.model.Patient;
import com.medical.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper mapper;

    @Autowired
    private PagedResourcesAssembler<Message> assembler;

    public MessageResponseDTO addMessage(MessagePostDTO messagePostDTO){
        Message message = mapper.DtoToEntity(messagePostDTO);
        message.setDate(LocalDateTime.now());
        return mapper.toModel(messageRepository.save(message));
    }

    public PagedModel<MessageResponseDTO> getListMessage(Long sender_id, Long receiver_id, Pageable pageable){
        return assembler.toModel(messageRepository.findAllOfUserandUser(sender_id,receiver_id,pageable),mapper);
    }
}
