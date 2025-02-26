package es.dws.gym.gym.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    private String userName;
    private String content;
    private LocalDateTime date;

    public Review(@JsonProperty("userName") String userName,@JsonProperty("content") String content, @JsonProperty("date") LocalDateTime date){
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

    public LocalDateTime getLocalDateTime() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLocalDateTime(LocalDateTime date) {
        this.date = date;
    }
}
