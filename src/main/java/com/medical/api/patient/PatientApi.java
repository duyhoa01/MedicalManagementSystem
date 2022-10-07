package com.medical.api.patient;

import com.medical.dtos.PatientModel;
import com.medical.dtos.modelAssembler.PatientModelAssembler;
import com.medical.model.Patient;
import com.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientApi {

    @Autowired
    private PatientService patientService;

    @PostMapping("")
    public ResponseEntity<?> addPatient(@RequestBody @Valid Patient patient){
        PatientModelAssembler patientModelAssembler=new PatientModelAssembler();
        Patient newPatient = patientService.addPatient(patient);

        if(newPatient == null){
            return new ResponseEntity<>("create fail",HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<PatientModel>(patientModelAssembler.toModel(patient),HttpStatus.OK);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePatient(@RequestBody @Valid Patient patient, @PathVariable @Valid Long id){
        PatientModelAssembler patientModelAssembler=new PatientModelAssembler();

        Patient upPatient= patientService.updatePatient(patient,id);
        if(upPatient == null){
            return new ResponseEntity<>("update fail",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<PatientModel>(patientModelAssembler.toModel(upPatient),HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPatient(@PathVariable Long id){
        PatientModelAssembler patientModelAssembler = new PatientModelAssembler();

        Patient patient = patientService.getPatientById(id);

        if(patient == null){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<PatientModel>(patientModelAssembler.toModel(patient),HttpStatus.OK);
        }
    }

    @GetMapping("")
    public ResponseEntity<PagedModel<Patient>> getListPatient(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(required = false, defaultValue ="") String key,
                                                              @RequestParam(required = false, defaultValue ="user.lastName,asc") String sort,
                                                              PagedResourcesAssembler assembler){

        PatientModelAssembler patientModelAssembler = new PatientModelAssembler();

        Page<Patient> patients = patientService.getListPatient(page,size,key,sort);

        if (patients ==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<PagedModel<Patient>>(assembler.toModel(patients,patientModelAssembler),HttpStatus.OK);
    }
}
