package es.dws.gym.gym.user;

public class User {
    private String firshName;
    private String secondName;
    private String telephone;
    private String mail;
    private String address;
    private String password;

    public User(String firshName, String secondName, String telephone, String mail, String address, String password){
        this.firshName = firshName;
        this.secondName = secondName;
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
        this.password = password;
    }

    public String getfirshName() {
        return firshName;
    }

    public String getsecondName() {
        return secondName;
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

    public void setfirshName(String firshName) {
        this.firshName = firshName;
    }

    public void setsecondName(String secondName) {
        this.secondName = secondName;
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
