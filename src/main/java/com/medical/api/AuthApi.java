package com.medical.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.dtos.AuthRequest;
import com.medical.dtos.MessageResponse;
import com.medical.dtos.PatientPostDTO;
import com.medical.dtos.PatientResponeDTO;
import com.medical.filter.JwtTokenUtil;
import com.medical.model.User;
import com.medical.service.PatientService;
import com.medical.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class AuthApi {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PatientService patientService;


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(), authRequest.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String access_token = jwtTokenUtil.generateAccessToken(user,request);
            String refresh_token=jwtTokenUtil.generateRefreshToken(user,request);
            Map<String,String> tokens=new HashMap<>();
            tokens.put("user_id",user.getId()+"");
            tokens.put("email",user.getEmail());
            tokens.put("role",user.getRole().getName());
            tokens.put("access_token",access_token);
            tokens.put("refresh_token",refresh_token);
            tokens.put("image",user.getImage());
            if(user.getRole().getName().equals("ROLE_DOCTOR")){
                tokens.put("id",user.getDoctor().getId()+"");
            } else if(user.getRole().getName().equals("ROLE_PATIENT")){
                tokens.put("id",user.getPatient().getId()+"");
            }

            return ResponseEntity.ok().body(tokens);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader=request.getHeader(AUTHORIZATION);
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token=authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = jwtTokenUtil.getDecodedJWT(refresh_token);
                String username=decodedJWT.getSubject();
                User user=userService.getUserByUserName(username);
                String access_token= jwtTokenUtil.generateAccessToken(user,request);
                Map<String,String> tokens=new HashMap<>();
                tokens.put("username",user.getUsername());
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error=new HashMap<>();
                error.put("error_token",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestPart @Valid PatientPostDTO patientPostDTO, @RequestPart(required = false) MultipartFile image){
        if( patientPostDTO.getUser().getPassword() == null){
            return new ResponseEntity<>(new MessageResponse("nhập thiếu password"),HttpStatus.BAD_REQUEST);
        }
        patientPostDTO.getUser().setStatus(false);
        patientPostDTO.getUser().setPassword(passwordEncoder.encode(patientPostDTO.getUser().getPassword()));

        try {
            PatientResponeDTO newPatient = patientService.addPatient(patientPostDTO,image);
            return new ResponseEntity<>(newPatient,HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.CONFLICT);
        } catch (SendFailedException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/auth/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        try {
            String res= userService.confirmToken(token);
            Map<String,String> response=new HashMap<>();
            response.put("message",res);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
