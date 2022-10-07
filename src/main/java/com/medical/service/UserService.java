package com.medical.service;

import com.medical.model.User;
import com.medical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in database");
        }
        return user;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserByUserName(String username){
        return userRepository.findByUsername(username);
    }

    public boolean selectExistsUserName(String username){
        return userRepository.selectExistsUserName(username);
    }

    public User addUser(User user){
        Boolean existsUser = userRepository
                .selectExistsUserName(user.getUsername());
        if(existsUser){
            return null;
        }
        return userRepository.save(user);
    }

    public User updateUser(User user,Long id){
        try{
            User upUser=userRepository.findById(id).get();

            upUser.setStatus(user.getStatus());
            upUser.setPhoneNumber(user.getPhoneNumber());
            upUser.setSex(user.getSex());
            upUser.setLastName(user.getLastName());
            upUser.setFirstName(user.getFirstName());
            upUser.setAge(user.getAge());

            return userRepository.save(upUser);

        } catch (Exception e){

        }
        return null;
    }

    public boolean deleteUser(Long id){
        try{
            User user = userRepository.findById(id).get();

            if(user == null) {
                return false;
            }

            userRepository.delete(user);

            return true;
        } catch (Exception e){

        }

        return false;
    }
}
