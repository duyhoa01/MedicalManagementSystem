package com.medical.api.specialty;

import com.medical.dtos.SpecialtyDTO;
import com.medical.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyApi {
    @Autowired
    private SpecialtyService specialtyService;

    @PostMapping("")
    public ResponseEntity<?> addSpecialty(@RequestBody @Valid SpecialtyDTO specialtyDTO){
        var resData=specialtyService.AddSpecialty(specialtyDTO);
        if(resData == null){
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<SpecialtyDTO>(resData, HttpStatus.OK);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListSpecialty(@RequestParam(required = false, defaultValue = "") String key){
        return new ResponseEntity<>(specialtyService.getAllSpecialty(key),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSpecialtyById(@PathVariable Long id){
        SpecialtyDTO specialtyDTO = specialtyService.getSpecialtyById(id);
        if(specialtyDTO == null){
            return new ResponseEntity<>("id specialty khong ton tai",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(specialtyDTO,HttpStatus.OK);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSpecialty(@PathVariable Long id, @RequestBody SpecialtyDTO specialtyDTO){
        SpecialtyDTO upSpecialty = specialtyService.updateSpecialty(id,specialtyDTO);
        if(upSpecialty == null){
            return new ResponseEntity<>("id specialty khong ton tai",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(specialtyDTO,HttpStatus.OK);
        }
    }
}
