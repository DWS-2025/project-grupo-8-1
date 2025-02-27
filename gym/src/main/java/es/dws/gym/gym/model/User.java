package es.dws.gym.gym.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String firstName;
    private String sureName;
    private String telephone;
    private String mail;
    private String address;
    private String password;

    public User(@JsonProperty("firstName") String firstName, @JsonProperty("sureName") String sureName,@JsonProperty("telephone") String telephone,@JsonProperty("mail") String mail,@JsonProperty("address") String address,@JsonProperty("password") String password){
        this.firstName = firstName;
        this.sureName = sureName;
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
        this.password = password;
    }

    public String getfirstName() {
        return firstName;
    }

    public String getsureName() {
        return sureName;
    }

    public String getAddress() {
        return address;
    }

    public String getMail() {
        return mail;
    }
    
    public String getTelephone() {
        return telephone;
    }

    public String getPassword() {
        return password;
    }

    public List<String> ListUser(){
        List<String> list = new ArrayList<>();
        list.add(firstName);
        list.add(sureName);
        list.add(telephone);
        list.add(mail);
        list.add(address);
        return list;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setsureName(String sureName) {
        this.sureName = sureName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
