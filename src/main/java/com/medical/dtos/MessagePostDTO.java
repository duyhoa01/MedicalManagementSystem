package com.medical.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePostDTO {

    private String message;

    private String image;

    private String sender;

    private String receiver;
}
