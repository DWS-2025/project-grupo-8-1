package es.dws.gym.gym.manager;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.User;

/**
 * UserManager is responsible for handling user-related operations.
 * It allows adding, removing, retrieving, and authenticating users.
 */
@Service
public class UserManager extends BaseManager {

    // Stores users mapped by their usernames
    private Map<String, User> users = new HashMap<>();

    /**
     * Constructor for UserManager.
     * Loads existing users from storage.
     */
    public UserManager() {
        this.users = readFromDisk(FilesWeb.USERSMAPFILE, new TypeReference<Map<String, User>>() {});
    }

    /**
     * Adds a new user to the system if the username is not already taken.
     *
     * @param userName  The unique username for the user.
     * @param firstName The user's first name.
     * @param sureName  The user's surname.
     * @param telephone The user's phone number.
     * @param mail      The user's email address.
     * @param address   The user's home address.
     * @param password  The user's password.
     * @return True if the user was successfully added, false if the username is already in use.
     */
    public boolean addUser(String userName, String firstName, String sureName, String telephone, String mail, String address, String password) {
        if (!users.containsKey(userName)) {
            User user = new User(firstName, sureName, telephone, mail, address, password);
            this.users.put(userName, user);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }
        return false;
    }

    /**
     * Removes a user from the system.
     *
     * @param userName The username of the user to be removed.
     * @return True if the user was found and removed, false otherwise.
     */
    public boolean removeUser(String userName) {
        if (users.containsKey(userName)) {
            this.users.remove(userName);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }
        return false;
    }

    /**
     * Authenticates a user by checking if the username and password match.
     *
     * @param name     The username of the user.
     * @param password The password provided for authentication.
     * @return True if the credentials are correct, false otherwise.
     */
    public boolean login(String name, String password) {
        if (!users.containsKey(name)) {
            return false;
        }
        User user = users.get(name);
        return user.getPassword().equals(password);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param name The username of the user.
     * @return The User object if found, otherwise null.
     */
    public User getUser(String name) {
        return this.users.get(name);
    }

    /**
     * Checks if a user exists in the system.
     *
     * @param name The username to check.
     * @return True if the user exists, false otherwise.
     */
    public boolean isUser(String name) {
        return users.containsKey(name);
    }

    /**
     * Retrieves a list of user details.
     *
     * @param name The username of the user.
     * @return A list containing user information.
     */
    public List<String> getUserList(String name) {
        User user = getUser(name);
        return user.listUser();
    }

    /**
     * Updates the password of a user.
     *
     * @param name     The username of the user.
     * @param password The new password to set.
     */
    public void setPassword(String name, String password) {
        users.get(name).setPassword(password);
        writeToDisk(FilesWeb.USERSMAPFILE, this.users);
    }
}
