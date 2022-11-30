package com.medical.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
@JsonRootName(value = "message")
@Relation(collectionRelation = "messages")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class MessageResponseDTO extends RepresentationModel<MessageResponseDTO> {
    private Long id;
    private String message;
    private String sender_name;
    private String sender;
    private String receiver_name;
    private String receiver;
    private LocalDateTime date;
    private String image;
}
