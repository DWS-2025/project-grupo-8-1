package es.dws.gym.gym.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dws.gym.gym.dto.CreateUserDTO;
import es.dws.gym.gym.dto.UpdateUserDTO;
import es.dws.gym.gym.dto.ViewUserDTO;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.UserRepository;

/* 
 * UserService.java
 * 
 * This service class handles user-related operations such as creating, updating,
 * deleting, and retrieving user information. It interacts with the UserRepository
 * to perform CRUD operations on the User entity.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // This method retrieves all users from the database and converts them to ViewUserDTO objects.
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

    // This method retrieves a user by ID and converts it to a ViewUserDTO object.
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

    // This method creates a new user based on the provided CreateUserDTO object.
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
                    null,
                    "USER"
            );
            return newUser;
        } catch (IOException e) {
            return null;
        }
    }

    // This method updates an existing user based on the provided UpdateUserDTO object.
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

    // This method deletes a user by ID. It returns true if the user was deleted successfully, false otherwise.
    public boolean deleteUser(String id) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        removeUser(user);
        return true;
    }

    // This method deletes a user by ID and returns the deleted user's information as a ViewUserDTO object.
    public ViewUserDTO deleteUserAndReturnDTO(String id) {
        User user = getUser(id);
        if (user == null) {
            return null;
        }
        ViewUserDTO userDTO = getViewUserAsDTO(id);
        removeUser(user);
        return userDTO;
    }

    // This method converts a User object to an UpdateUserDTO object.
    private UpdateUserDTO convertToUpdateDTO(User user) {
        return new UpdateUserDTO(
                user.getFirstName(),
                user.getSureName(),
                user.getTelephone(),
                user.getMail(),
                user.getAddress()
        );
    }

    // This method adds a new user to the database. It returns true if the user was added successfully, false otherwise.
    public boolean addUser(String id, String firstName, String sureName, String telephone, String mail, String address, String password, MultipartFile imageUpload, String rol) throws IOException {
        if (isUser(id)) {
            return false;
        }

        Blob imageUser = null;
        if (imageUpload != null && !imageUpload.isEmpty()) {
            imageUser = BlobProxy.generateProxy(imageUpload.getInputStream(), imageUpload.getSize());
        }

        User user = new User(id, firstName, sureName, telephone, mail, address, passwordEncoder.encode(password), imageUser,rol);
        userRepository.save(user);
        return true;
    }

    // This method checks if a user exists in the database by ID.
    public boolean isUser(String id) {
        return userRepository.existsById(id);
    }

    // This method retrieves a user by ID from the database.
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    // This method retrieves a user by ID and converts it to a User object.
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

    // This method retrieves a user by ID and converts it to a User object.
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    // This method retrieves all users from the database.
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // This method retrieves a user by ID and converts it to a User object.
    public boolean setPassword(String id, String newPassword) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
