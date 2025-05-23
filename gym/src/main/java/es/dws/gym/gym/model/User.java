package es.dws.gym.gym.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

/**
 * Represents a user with personal details such as name, contact information, and password.
 */

@Entity(name="webUser")
 public class User {
    
    @Id
    @Column(unique = true, nullable = false)
    // User's username
    private String id;

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
    
    // User's reviews
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    // User's gym classes
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Gimclass> gimclass;

    // User's profile image
    @Lob
    private Blob imageUser;

    // User's role
    private String rol;

    public User(){}

    // Constructor to initialize a User object.
    public User(String id, String firstName, String sureName, String telephone, String mail, String address, String password, Blob imageUser, String rol) {
        this.id = id;
        this.firstName = firstName;
        this.sureName = sureName;
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
        this.password = password;
        this.reviews = new ArrayList<Review>();
        this.gimclass = new ArrayList<Gimclass>();
        this.imageUser = imageUser;
        this.rol = rol;
    }

    //Retrieves the user's ID.
    public String getId() {
        return id;
    }

    //Retrieves the user's first name.
    public String getFirstName() {
        return firstName;
    }

    //Retrieves the user's surname.
    public String getSureName() {
        return sureName;
    }

    //Retrieves the user's address.
    public String getAddress() {
        return address;
    }

    //Retrieves the user's email.
    public String getMail() {
        return mail;
    }

    //Retrieves the user's telephone number.
    public String getTelephone() {
        return telephone;
    }

    //Retrieves the user's password.
    public String getPassword() {
        return password;
    }

    //Retrieves the user's reviews.
    public List<Review> getReviews(){
        return reviews;
    }

    //Retrieves the user's gym classes.
    public List<Gimclass> getGimClass(){
        return gimclass;
    }

    //Retrieves the user's image.
    public Blob getImageUser() {
        return imageUser;
    }

    //Retrieves the user's role.
    public String getRolUser(){
        return rol;
    }

    //Updates the user's first name.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    //Updates the user's surname.
    public void setSureName(String sureName) {
        this.sureName = sureName;
    }


    //Updates the user's email.
    public void setMail(String mail) {
        this.mail = mail;
    }


    //Updates the user's telephone number.
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    // Updates the user's password.
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Updates the user's address.
    public void setAddress(String address) {
        this.address = address;
    }

    // Updates the user's reviews.
    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
    }

    // Updates the user's gym classes.
    public void setGimClass(List<Gimclass> gimclasses){
        this.gimclass = gimclasses;
    }

    // Updates the user's image.
    public void setImageUser(Blob imageUser) {
        this.imageUser = imageUser;
    }

    // Updates the user's role.
    public void setRolUser(String rol) {
        this.rol = rol;
    }

}
