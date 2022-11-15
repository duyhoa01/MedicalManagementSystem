package com.medical.api.doctor;

import com.medical.dtos.DoctorPostDTO;
import com.medical.dtos.DoctorResponseDTO;
import com.medical.dtos.MessageResponse;
import com.medical.dtos.PatientPostDTO;
import com.medical.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.SendFailedException;
import javax.print.Doc;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/doctors")
public class DoctorApi {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Boolean ok=true;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> addDoctor(@RequestPart @Valid DoctorPostDTO doctorPostDTO, @RequestPart(required = false) MultipartFile image) throws SendFailedException {

        System.out.println("add doctor");
        //hash password
        doctorPostDTO.getUser().setPassword(passwordEncoder.encode(doctorPostDTO.getUser().getPassword()));

        //call doctorService
        DoctorResponseDTO newDoctor=doctorService.addDoctor(doctorPostDTO,image);

        //check success add doctor
        if(newDoctor!=null){
            return new ResponseEntity<>(newDoctor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("username đã tồn tại hoặc mã chuyên khoa không đúng"),HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename){
        Resource file = doctorService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("")
    public ResponseEntity<PagedModel<DoctorResponseDTO>> getAllDoctor(Pageable pageable,
                                                                      @RequestParam(required = false, defaultValue ="") String key){

        return new ResponseEntity<PagedModel<DoctorResponseDTO>>(doctorService.getListDoctor(pageable,key),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try{
            doctorService.deleteDoctor(id);
            return new ResponseEntity<>(new MessageResponse("xóa thành công"),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_GATEWAY);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id){
        try{
            DoctorResponseDTO doctorModel = doctorService.getDoctorById(id);
            return new ResponseEntity<>(doctorModel,HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
//    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<?> updateDoctor(@RequestPart @Valid DoctorPostDTO doctorPostDTO,
                                          @RequestPart(required = false) MultipartFile image,
                                          @PathVariable Long id){
        try{
            DoctorResponseDTO upDoctor=doctorService.updateDoctor(doctorPostDTO,image,id);
            return new ResponseEntity<>(upDoctor,HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}
