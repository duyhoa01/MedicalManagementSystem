package com.medical.dtos.modelAssembler;

import com.medical.dtos.DoctorModel;
import com.medical.dtos.UserModel;
import com.medical.model.Doctor;
import com.medical.model.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {

    @Override
    public UserModel toModel(User entity) {

        UserModel userModel=new UserModel();
        userModel.setId(entity.getId());
        userModel.setAge(entity.getAge());
        userModel.setEmail(entity.getEmail());
        userModel.setUsername(entity.getUsername());
        userModel.setFirstName(entity.getFirstName());
        userModel.setLastName(entity.getLastName());
        userModel.setSex(entity.getSex());
        userModel.setStatus(entity.getStatus());
        userModel.setPhoneNumber(entity.getPhoneNumber());

        return  userModel;
    }

    public User convertToUser(UserModel userModel){
        User user=new User();

        user.setPassword(userModel.getPassword());
        user.setAge(userModel.getAge());
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setUsername(userModel.getUsername());
        user.setLastName(userModel.getLastName());
        user.setSex(userModel.getSex());
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setStatus(userModel.getStatus());

        return user;
    }


}
