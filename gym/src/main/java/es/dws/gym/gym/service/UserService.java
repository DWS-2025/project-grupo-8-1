package es.dws.gym.gym.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dws.gym.gym.dto.CreateUserDTO;
import es.dws.gym.gym.dto.UpdateUserDTO;
import es.dws.gym.gym.dto.ViewUserDTO;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<ViewUserDTO> getAllViewUsersDTO(){
        return userRepository.findAll().stream()
            .map(user -> new ViewUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getSureName(),
                user.getTelephone(),
                user.getMail(),
                user.getAddress(),
                "/user/" + user.getId() + "/image"
            ))
            .collect(Collectors.toList());
    }


    public ViewUserDTO getViewUserAsDTO(String id){
        User user = getUser(id);
        return user == null ? null : new ViewUserDTO(
            user.getId(),
            user.getFirstName(),
            user.getSureName(),
            user.getTelephone(),
            user.getMail(),
            user.getAddress(),
            "/user/" + user.getId() + "/image"
        );
    }

    public CreateUserDTO createUser(CreateUserDTO newUser) {
        if (isUser(newUser.id())) {
            return null;
        }
        try {
            addUser(
                    newUser.id(),
                    newUser.firstName(),
                    newUser.sureName(),
                    newUser.telephone(),
                    newUser.mail(),
                    newUser.address(),
                    newUser.password(),
                    null
            );
            return newUser;
        } catch (IOException e) {
            return null;
        }
    }

    public UpdateUserDTO updateUser(String id, UpdateUserDTO updatedUser) {
        if (!isUser(id)) {
            return null;
        }
        try {
            editUser(
                    id,
                    updatedUser.firstName(),
                    updatedUser.sureName(),
                    updatedUser.telephone(),
                    updatedUser.mail(),
                    updatedUser.address(),
                    null
            );
            User user = getUser(id);
            return user == null ? null : convertToUpdateDTO(user);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        removeUser(user);
        return true;
    }

    public ViewUserDTO deleteUserAndReturnDTO(String id) {
        User user = getUser(id);
        if (user == null) {
            return null;
        }
        ViewUserDTO userDTO = getViewUserAsDTO(id);
        removeUser(user);
        return userDTO;
    }

    private UpdateUserDTO convertToUpdateDTO(User user) {
        return new UpdateUserDTO(
                user.getFirstName(),
                user.getSureName(),
                user.getTelephone(),
                user.getMail(),
                user.getAddress()
        );
    }

    public boolean addUser(String id, String firstName, String sureName, String telephone, String mail, String address, String password, MultipartFile imageUpload) throws IOException {
        if (isUser(id)) {
            return false;
        }

        Blob imageUser = null;
        if (imageUpload != null && !imageUpload.isEmpty()) {
            imageUser = BlobProxy.generateProxy(imageUpload.getInputStream(), imageUpload.getSize());
        }

        User user = new User(id, firstName, sureName, telephone, mail, address, password, imageUser);
        userRepository.save(user);
        return true;
    }

    public boolean isUser(String id) {
        return userRepository.existsById(id);
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void editUser(String id, String firstName, String sureName, String telephone, String mail, String address, MultipartFile imageUpload) throws IOException {
        User user = getUser(id);
        user.setFirstName(firstName);
        user.setSureName(sureName);
        user.setTelephone(telephone);
        user.setMail(mail);
        user.setAddress(address);
        if (imageUpload != null && !imageUpload.isEmpty()) {
            Blob imageUser = BlobProxy.generateProxy(imageUpload.getInputStream(), imageUpload.getSize());
            user.setImageUser(imageUser);
        }
        userRepository.save(user);
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean setPassword(String id, String newPassword) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }
}
