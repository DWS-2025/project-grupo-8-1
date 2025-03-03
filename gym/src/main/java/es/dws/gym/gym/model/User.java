package es.dws.gym.gym.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user with personal details such as name, contact information, and password.
 */
public class User {

    // User's first name
    private String firstName;

    // User's surname
    private String sureName;

    // User's telephone number
    private String telephone;

    // User's email address
    private String mail;

    // User's physical address
    private String address;

    // User's password
    private String password;

    /**
     * Constructor to initialize a User object.
     *
     * @param firstName The first name of the user.
     * @param sureName  The surname of the user.
     * @param telephone The user's telephone number.
     * @param mail      The user's email address.
     * @param address   The user's physical address.
     * @param password  The user's password.
     */
    public User(@JsonProperty("firstName") String firstName,
                @JsonProperty("sureName") String sureName,
                @JsonProperty("telephone") String telephone,
                @JsonProperty("mail") String mail,
                @JsonProperty("address") String address,
                @JsonProperty("password") String password) {
        this.firstName = firstName;
        this.sureName = sureName;
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
        this.password = password;
    }

    /**
     * Retrieves the user's first name.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the user's surname.
     *
     * @return The surname.
     */
    public String getSureName() {
        return sureName;
    }

    /**
     * Retrieves the user's address.
     *
     * @return The physical address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the user's email.
     *
     * @return The email address.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Retrieves the user's telephone number.
     *
     * @return The telephone number.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns a list of user details.
     *
     * @return A list containing the user's personal information.
     */
    public List<String> listUser() {
        List<String> list = new ArrayList<>();
        list.add(firstName);
        list.add(sureName);
        list.add(telephone);
        list.add(mail);
        list.add(address);
        return list;
    }

    /**
     * Updates the user's first name.
     *
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Updates the user's surname.
     *
     * @param sureName The new surname.
     */
    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    /**
     * Updates the user's email.
     *
     * @param mail The new email address.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Updates the user's telephone number.
     *
     * @param telephone The new telephone number.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Updates the user's password.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
