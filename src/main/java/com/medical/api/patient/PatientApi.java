package com.medical.api.patient;

import com.medical.dtos.DoctorResponseDTO;
import com.medical.dtos.MessageResponse;
import com.medical.dtos.PatientResponeDTO;
import com.medical.dtos.PatientPostDTO;
import com.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.SendFailedException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/patients")
public class PatientApi {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("")
    public ResponseEntity<?> addPatient(@RequestPart @Valid PatientPostDTO patientPostDTO, @RequestPart(required = false) MultipartFile image){
        if(patientPostDTO.getUser().getUsername()==null || patientPostDTO.getUser().getPassword() == null){
            return new ResponseEntity<>(new MessageResponse("nhập đầy đủ thông tin"),HttpStatus.BAD_REQUEST);
        }
        patientPostDTO.getUser().setStatus(true);
        patientPostDTO.getUser().setPassword(passwordEncoder.encode(patientPostDTO.getUser().getPassword()));

        try {
            PatientResponeDTO newPatient = patientService.addPatient(patientPostDTO,image);
            return new ResponseEntity<>(newPatient,HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.CONFLICT);
        } catch (SendFailedException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("{id}")
//    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<?> updatePatient(@RequestPart @Valid PatientPostDTO patientPostDTO,
                                           @RequestPart(required = false) MultipartFile image,
                                           @PathVariable Long id){
        try{
            PatientResponeDTO upPatient= patientService.updatePatient(patientPostDTO,image,id);
            return new ResponseEntity<>(upPatient,HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or #username == authentication.name")
    public ResponseEntity<?> getPatient(@PathVariable Long id){
        try{
            PatientResponeDTO patient = patientService.getPatientById(id);
            return new ResponseEntity<>(patient,HttpStatus.OK);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getListPatient(Pageable pageable,
                                            @RequestParam(required = false, defaultValue ="") String key){

        try{
            return new ResponseEntity<PagedModel<PatientResponeDTO>>(patientService.getListPatient(pageable,key),HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        try{
            patientService.deletePatient(id);
            return new ResponseEntity<>(new MessageResponse("xóa thành công"),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_GATEWAY);
        }
    }

}
