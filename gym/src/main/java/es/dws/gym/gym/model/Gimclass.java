package es.dws.gym.gym.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// Represents a gym class with its details such as name, description, schedule, and duration.
@Entity(name="GimclassTable")
public class Gimclass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // Name of the gym class
    private String name;

    // Description of the class
    private String descrip;

    @ManyToMany
    private List<User> users;

    // Date and time when the class is scheduled
    private Date time;

    // Duration of the class
    private Time duration;

    public Gimclass(){}

    //Constructor to initialize a gym class.
    public Gimclass(String name, String descript, Date time, Time duration) {
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

    public List<User> getUsers(){
        return users;
    }
}
