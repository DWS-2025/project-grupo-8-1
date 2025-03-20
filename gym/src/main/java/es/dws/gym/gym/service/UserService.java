package es.dws.gym.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public boolean addUser(String id, String firstName, String sureName, String telephone, String mail, String address, String password){
        if(isUser(id)){
            return false;
        }
        User user = new User(id,firstName,sureName,telephone,mail,address,password);
        userRepository.save(user);
        return true;
    }

    public boolean isUser(String id){
        return userRepository.existsById(id);
    }

    public User getUser(String id){
        return userRepository.findById(id).orElse(null);
    }

    public boolean setPassword(String id, String password){
        User user = getUser(id);
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }

    public boolean setfirstName(String id, String firstName){
        User user = getUser(id);
        user.setFirstName(firstName);
        userRepository.save(user);
        return true;
    }

    public boolean setSureName(String id, String sureName){
        User user = getUser(id);
        user.setSureName(sureName);
        userRepository.save(user);
        return true;
    }

    public boolean setTelephone(String id, String telephone){
        User user = getUser(id);
        user.setTelephone(telephone);
        userRepository.save(user);
        return true;
    }

    public boolean setMail(String id, String mail){
        User user = getUser(id);
        user.setMail(mail);
        userRepository.save(user);
        return true;
    }

    public boolean setAddress(String id, String address){
        User user = getUser(id);
        user.setAddress(address);
        userRepository.save(user);
        return true;
    }
}
