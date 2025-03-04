package es.dws.gym.gym.model;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonProperty;

// Represents a gym class with its details such as name, description, schedule, and duration.
public class Gimclass {
    
    // Name of the gym class
    private String name;

    // Description of the class
    private String descrip;

    // Date and time when the class is scheduled
    private Date time;

    // Duration of the class
    private Time duration;


    //Constructor to initialize a gym class.
    public Gimclass(@JsonProperty("name") String name,@JsonProperty("descript") String descript,@JsonProperty("time") Date time,@JsonProperty("duration") Time duration) {
        this.name = name;
        this.descrip = descript;
        this.time = time;
        this.duration = duration;
    }

    // Retrieves the name of the class.
    public String getName() {
        return name;
    }

    // Retrieves the description of the class.
    public String getDescrip() {
        return descrip;
    }

    //Retrieves the duration of the class.
    public Time getDuration() {
        return duration;
    }

    // Retrieves the scheduled date and time of the class.
    public Date getTime() {
        return time;
    }
}
