package com.medical.mapper;

import com.medical.dtos.MessagePostDTO;
import com.medical.dtos.MessageResponseDTO;
import com.medical.model.Message;
import com.medical.model.User;
import com.medical.repository.MessageRepository;
import com.medical.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.NoSuchElementException;
import java.util.Optional;

public class MessageMapper implements RepresentationModelAssembler<Message, MessageResponseDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MessageResponseDTO toModel(Message entity) {

        MessageResponseDTO message = new MessageResponseDTO();
        message.setReceiver_name(entity.getReceiver().getFirstName()+" "+entity.getReceiver().getLastName());
        message.setReceiver(entity.getReceiver().getId()+"");
        message.setSender_name(entity.getSender().getFirstName()+" "+entity.getSender().getLastName());
        message.setSender(entity.getSender().getId()+"");
        message.setMessage(entity.getMessage());
        message.setId(entity.getId());
        message.setDate(entity.getDate());
        message.setImage(entity.getImage());

        return message;
    }

    public Message DtoToEntity(MessagePostDTO messagePostDTO){
        Message message =  mapper.map(messagePostDTO,Message.class);
        Optional<User> sender = userRepository.findById(Long.valueOf(messagePostDTO.getSender()));
        if(sender.isEmpty()){
            throw new NoSuchElementException("id sender không tôn tại");
        }
        Optional<User> receiver = userRepository.findById(Long.valueOf(messagePostDTO.getReceiver()));
        if(receiver.isEmpty()){
            throw new NoSuchElementException("id receiver không tôn tại");
        }
        message.setSender(sender.get());
        message.setReceiver(receiver.get());
        return message;
    }
}
