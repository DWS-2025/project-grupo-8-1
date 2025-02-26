package es.dws.gym.gym.model;

import java.sql.Date;

public class Review {
    private String userName;
    private String content;
    private Date date;

    public Review(String userName, String content, Date date){
        this.userName = userName;
        this.content = content;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
