package es.dws.gym.gym.model;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name="webReview")
// Represents a user review with details such as username, content, and date of review.
public class Review {
    
    // Unique identifier for the review
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // The user who posted the review
    @ManyToOne
    private User users;

    // The content of the review
    private String content;

    // The date when the review was posted
    private Date date;

    public Review(){}

    //Constructor to initialize a review.
    public Review(String content, Date date) {
        this.content = content;
        this.date = date;
    }

    // Retrieves the ID of the review.
    public Long getId() {
        return id;
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


    //Updates the date of the review.
    public void setLocalDateTime(Date date) {
        this.date = date;
    }

    // Gets the user who posted the review.
    public User getUser(){
        return users;
    }

    // Sets the user who posted the review.
    public void setUser(User users){
        this.users = users;
    }

    // Checks if the review was posted by a specific user.
    public boolean isAutorReview(User users){
        return this.users.equals(users);
    }
}
