package es.dws.gym.user;

public class user {
    private String telephone;
    private String mail;
    private String address;
    private String password;

    public user(int id, String telephone, String mail, String address, String password){
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
        this.password = password;
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
