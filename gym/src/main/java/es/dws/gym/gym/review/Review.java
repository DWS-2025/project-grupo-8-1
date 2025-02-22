package es.dws.gym.gym.review;

import java.sql.Date;

public class Review {
    private int id;
    private String content;
    private Date date;

    public Review(int id, String content, Date date){
        this.id = id;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
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

    public void setDate(Date date) {
        this.date = date;
    }
}
