package com.medical.api;


import com.medical.dtos.AuthRequest;
import com.medical.dtos.ChangePassDTO;
import com.medical.dtos.MessageResponse;
import com.medical.model.User;
import com.medical.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<List<User>> getUser(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

//    @PostMapping("/save")
//    public ResponseEntity<User> saveUser(@RequestBody User user){
//        URI uri=URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return ResponseEntity.created(uri).body(userService.saveUser(user));
//    }

    @GetMapping("/block/{username}")
    @PreAuthorize("hasRole('ADMIN') and #username != authentication.name")
    public ResponseEntity<?> blockUser(@PathVariable String username){
        boolean ok = userService.blockUser(username);
        if(ok){
            return new ResponseEntity<>(new MessageResponse("block user success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("block user fail"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/enable/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enableUser(@PathVariable String username){
        boolean ok = userService.enableUser(username);
        if(ok){
            return new ResponseEntity<>(new MessageResponse("enable user success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("enable user fail"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password")
    @PreAuthorize("#changePassDTO.username == authentication.name")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassDTO changePassDTO){
        changePassDTO.setPassword(passwordEncoder.encode(changePassDTO.getPassword()));
        boolean ok = userService.changeassword(changePassDTO.getPassword(),changePassDTO.getUsername(), changePassDTO.getOldPassword());
        if(ok==false){
            return new ResponseEntity<>(new MessageResponse("update fail"),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new MessageResponse("change password success"), HttpStatus.OK);
        }
    }
}
