package es.dws.gym.gym.manager;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.User;

// UserManager is responsible for handling user-related operations.
//It allows adding, removing, retrieving, and authenticating users.

@Service
public class UserManager extends BaseManager {

    // Stores users mapped by their usernames
    private Map<String, User> users = new HashMap<>();


    // Constructor for UserManager.
    public UserManager() {
        this.users = readFromDisk(FilesWeb.USERSMAPFILE, new TypeReference<Map<String, User>>() {});
    }

    // Adds a new user to the system if the username is not already taken.
    public boolean addUser(String userName, String firstName, String sureName, String telephone, String mail, String address, String password) {
        if (!users.containsKey(userName)) {
            User user = new User(firstName, sureName, telephone, mail, address, password);
            this.users.put(userName, user);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }
        return false;
    }

    //Removes a user from the system.
    public boolean removeUser(String userName) {
        if (users.containsKey(userName)) {
            this.users.remove(userName);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }
        return false;
    }

    // Authenticates a user by checking if the username and password match.
    public boolean login(String name, String password) {
        if (!users.containsKey(name)) {
            return false;
        }
        User user = users.get(name);
        return user.getPassword().equals(password);
    }

    //Retrieves a user by their username.
    public User getUser(String name) {
        return this.users.get(name);
    }

    // Checks if a user exists in the system.
    public boolean isUser(String name) {
        return users.containsKey(name);
    }

    // Retrieves a list of user details.
    public List<String> getUserList(String name) {
        User user = getUser(name);
        return user.listUser();
    }

    // Updates the password of a user.
    public void setPassword(String name, String password) {
        users.get(name).setPassword(password);
        writeToDisk(FilesWeb.USERSMAPFILE, this.users);
    }
}
