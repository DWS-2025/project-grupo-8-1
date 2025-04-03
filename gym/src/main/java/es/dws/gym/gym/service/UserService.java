package es.dws.gym.gym.service;

import java.io.IOException;
import java.sql.Blob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public boolean addUser(String id, String firstName, String sureName, String telephone, String mail, String address, String password, MultipartFile imageUpload) throws IOException {
        if(isUser(id)){
            return false;
        }
        
        Blob imageUser = null;
        if(imageUpload != null && !imageUpload.isEmpty()){
            imageUser = BlobProxy.generateProxy(imageUpload.getInputStream(), imageUpload.getSize());
        }
        
        User user = new User(id, firstName, sureName, telephone, mail, address, password, imageUser);
        userRepository.save(user);
        return true;
    }

    public boolean isUser(String id){
        return userRepository.existsById(id);
    }

    public User getUser(String id){
        return userRepository.findById(id).orElse(null);
    }

    public void setPassword(String id, String password){
        User user = getUser(id);
        user.setPassword(password);
        userRepository.save(user);
    }

    public void editUser(String id, String firstName, String sureName, String telephone, String mail, String address, MultipartFile imageUpload) throws IOException {
        User user = getUser(id);
        user.setFirstName(firstName);
        user.setSureName(sureName);
        user.setTelephone(telephone);
        user.setMail(mail);
        user.setAddress(address);
        if(imageUpload != null && !imageUpload.isEmpty()){
            Blob imageUser = BlobProxy.generateProxy(imageUpload.getInputStream(), imageUpload.getSize());
            user.setImageUser(imageUser);
        }
        userRepository.save(user);
    }

    public void removeUser(User user){
        userRepository.delete(user);
    }
}
