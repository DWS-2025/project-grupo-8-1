package es.dws.gym.gym.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// Represents a gym class with its details such as name, description, schedule, and duration.
@Entity(name="GimclassTable")
public class Gimclass {
    
    // Unique identifier for the gym class
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // Name of the gym class
    private String name;

    // Description of the class
    private String descript;

    // List of users enrolled in the class
    @ManyToMany
    private List<User> users = new ArrayList<>();

    // Date and time when the class is scheduled
    private Date time;

    // Duration of the class
    private Time duration;

    // PDF file associated with the class, if any
    private String pdfFile;
    
    public Gimclass(){}

    //Constructor to initialize a gym class.
    public Gimclass(String name, String descript, Date time, Time duration) {
        this.name = name;
        this.descript = descript;
        this.time = time;
        this.duration = duration;
        this.pdfFile = null;
    }

    public Gimclass(String name, String descript, Date time, Time duration, String pdfFile) {
        this.name = name;
        this.descript = descript;
        this.time = time;
        this.duration = duration;
        this.pdfFile = pdfFile;
    }

    // Retrieves the ID of the class.
    public Long getId() {
        return id;
    }

    // Retrieves the name of the class.
    public String getName() {
        return name;
    }

    // Retrieves the description of the class.
    public String getDescrip() {
        return descript;
    }

    //Retrieves the duration of the class.
    public Time getDuration() {
        return duration;
    }

    // Retrieves the scheduled date and time of the class.
    public Date getTime() {
        return time;
    }

    // Retrieves the list of users enrolled in the class.
    public List<User> getUsers(){
        return users;
    }

    // Retrieves the PDF file associated with the class.
    public String getPdfFile() {
        return pdfFile;
    }

    // Sets the list of users enrolled in the class.
    public void setId(Long id) {
        this.id = id;
    }

    // Sets the name of the class.
    public void setName(String name) {
        this.name = name;
    }
    
    // Sets the description of the class.
    public void setDescript(String descript) {
        this.descript = descript;
    }

    // Sets the list of users enrolled in the class.
    public void setTime(Date time) {
        this.time = time;
    }

    // Sets the duration of the class.
    public void setDuration(Time duration) {
        this.duration = duration;
    }

    // Sets the PDF file associated with the class.
    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }
}
