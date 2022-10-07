package com.medical.service;

import com.medical.model.Role;
import com.medical.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getOrCreate(String name){
        Role role = roleRepository.findByName(name);
        if(role !=null){
            return  role;
        } else {
            Role newRole=new Role();
            newRole.setName(name);
            return roleRepository.save(newRole);
        }
    }

    public Role getRoleByName(String name){
        Role role = roleRepository.findByName(name);
        if(role!= null){
            return role;
        } else {
            return null;
        }
    }
}
