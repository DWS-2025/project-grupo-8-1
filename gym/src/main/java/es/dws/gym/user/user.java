package es.dws.gym.user;

public class user {
    private int id;
    private String telephone;
    private String mail;
    private String address;

    public user(int id, String telephone, String mail, String address){
        this.id = id;
        this.telephone = telephone;
        this.mail = mail;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }
    
    public String getTelephone() {
        return telephone;
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

}
