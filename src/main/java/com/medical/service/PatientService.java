package com.medical.service;

import com.medical.model.Patient;
import com.medical.model.User;
import com.medical.repository.PatientRepository;
import com.medical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public Patient addPatient(Patient patient){
        patient.getUser().setRole(roleService.getOrCreate("ROLE_PATIENT"));
        User user= userService.addUser(patient.getUser());
        if(user==null){
            return  null;
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Patient patient, Long id){
        try{
            Patient upPatient=patientRepository.findById(id).get();

            userService.updateUser(patient.getUser(),upPatient.getUser().getId());
            upPatient.setJob(patient.getJob());

            return patientRepository.save(upPatient);

        } catch (Exception e){

        }

        return null;
    }


    public Patient getPatientById(Long id){
        try{

            Patient patient= patientRepository.findById(id).get();

            return patient;

        } catch (Exception e){

        }

        return null;

    }

    public Page<Patient> getListPatient(int pageNumber, int sizePage, String key, String sort){
        String[] sorts= sort.split(",");
        Sort sort1;
        if(sorts.length==2 && (sorts[1].equals("acs") || sorts[1].equals("desc"))){
            switch (sorts[1]) {
                case "acs": {
                    sort1 = Sort.by(sorts[0]).ascending();
                    break;
                }
                case "desc": {
                    sort1 = Sort.by(sorts[0]).descending();
                    break;
                }
                default:{
                    sort1 = Sort.by(sorts[0]).descending();
                }
            }
        } else if(sorts.length>1) {
            sort1 = Sort.by(sorts[0]).ascending();
        } else {
            sort1 = Sort.by(sort).ascending();
        }
        try{
            if( key.equals("")){
                return patientRepository.findAll(PageRequest.of(pageNumber, sizePage, sort1));
            } else {
                return patientRepository.findListPatient(key,PageRequest.of(pageNumber, sizePage, sort1));
            }
        } catch (Exception e){

        }
        return null;

    }

    public boolean deletePatient(Long id){

        try{
            Patient patient = patientRepository.findById(id).get();
            if(patient == null){
                return false;
            }
            patientRepository.delete(patient);
            userService.deleteUser(patient.getUser().getId());
            return true;
        } catch (Exception e){

        }

        return false;
    }


}
