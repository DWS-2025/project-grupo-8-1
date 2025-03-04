package es.dws.gym.gym.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;


// Represents a user review with details such as username, content, and date of review.
public class Review {

    // The username of the person who wrote the review
    private String userName;

    // The content of the review
    private String content;

    // The date when the review was posted
    private Date date;

    //Constructor to initialize a review.
    public Review(@JsonProperty("userName") String userName,
                  @JsonProperty("content") String content,
                  @JsonProperty("date") Date date) {
        this.userName = userName;
        this.content = content;
        this.date = date;
    }


    //Retrieves the username of the reviewer.
    public String getUserName() {
        return userName;
    }


    // Retrieves the content of the review.
    public String getContent() {
        return content;
    }


    // Retrieves the date when the review was posted.
    public Date getLocalDateTime() {
        return date;
    }

    // Updates the content of the review.
    public void setContent(String content) {
        this.content = content;
    }


    //Updates the username of the reviewer.
    public void setUserName(String userName) {
        this.userName = userName;
    }


    //Updates the date of the review.
    public void setLocalDateTime(Date date) {
        this.date = date;
    }
}
