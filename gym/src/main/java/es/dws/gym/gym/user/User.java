package es.dws.gym.gym.user;

public class User {
    private String firstName;
    private String sureName;
    private String telephone;
    private String mail;
    private String address;
    private String password;

    public User(String firstName, String sureName, String telephone, String mail, String address, String password){
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

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setsureName(String sureName) {
        this.sureName = sureName;
    }

    public void setAddress(String address) {
        this.address = address;
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
