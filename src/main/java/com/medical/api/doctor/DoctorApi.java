package com.medical.api.doctor;

import com.medical.dtos.DoctorModel;
import com.medical.dtos.modelAssembler.DoctorModelAssembler;
import com.medical.model.Doctor;
import com.medical.model.User;
import com.medical.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/doctors")
public class DoctorApi {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN') and hasPermission('DOCTOR', 'ADD')")
    @RequestMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestPart DoctorModel doctorModel,@RequestPart MultipartFile file){

        DoctorModelAssembler doctorModelAssembler=new DoctorModelAssembler();

        //hash password
        doctorModel.getUser().setPassword(passwordEncoder.encode(doctorModel.getUser().getPassword()));
        //doctorModel.setImage(file);
        doctorModel.getUser().setStatus(false);
        //convert DoctorModel to Doctor
        Doctor doctor=doctorModelAssembler.convertToDoctor(doctorModel);
        System.out.println(doctor);

        //call doctorService
        Doctor newDoctor=doctorService.registerDoctor(doctor,file);

        //check success add doctor
        if(newDoctor!=null){
            return new ResponseEntity<>(doctorModelAssembler.toModel(newDoctor), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("create fail",HttpStatus.CONFLICT);
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
    public ResponseEntity<PagedModel<Doctor>> getAllDoctor(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam(required = false, defaultValue ="") String key,
                                                   @RequestParam(required = false, defaultValue = "rate") String sort,
                                                   PagedResourcesAssembler assembler){
        DoctorModelAssembler doctorModelAssembler=new DoctorModelAssembler();
        Page<Doctor> doctors= doctorService.getListDoctor(page,size,key,sort);
        return new ResponseEntity<PagedModel<Doctor>>(assembler.toModel(doctors,doctorModelAssembler),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasPermission('DOCTOR', 'DELETE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean ok = doctorService.deleteDoctor(id);
        if(ok){
            return new ResponseEntity<String>("delete success",HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<String>("delete not success",HttpStatus.RESET_CONTENT);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id){
        Doctor doctor = doctorService.getDoctorById(id);
        if(doctor==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            DoctorModelAssembler doctorModelAssembler=new DoctorModelAssembler();
            DoctorModel doctorModel= doctorModelAssembler.toModel(doctor);
            return new ResponseEntity<DoctorModel>(doctorModel,HttpStatus.OK);
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') and hasPermission('DOCTOR', 'UPDATE')")
    public ResponseEntity<?> updateDoctor(@RequestPart Doctor doctor,@PathVariable Long id){
        Doctor upDoctor=doctorService.updateDoctor(doctor,id);
        if(upDoctor == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            DoctorModelAssembler doctorModelAssembler=new DoctorModelAssembler();
            DoctorModel doctorModel= doctorModelAssembler.toModel(doctor);
            return new ResponseEntity<DoctorModel>(doctorModel,HttpStatus.OK);
        }
    }
}
