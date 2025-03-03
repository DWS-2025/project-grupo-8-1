package es.dws.gym.gym.model;

import java.sql.Date;
import java.sql.Time;

/**
 * Represents a gym class with its details such as name, description, schedule, and duration.
 */
public class Gimclass {

    // Name of the gym class
    private String name;

    // Description of the class
    private String descrip;

    // Date and time when the class is scheduled
    private Date time;

    // Duration of the class
    private Time duration;

    /**
     * Constructor to initialize a gym class.
     *
     * @param name     The name of the class.
     * @param descript The description of the class.
     * @param time     The date and time of the class.
     * @param duration The duration of the class.
     */
    public Gimclass(String name, String descript, Date time, Time duration) {
        this.name = name;
        this.descrip = descript;
        this.time = time;
        this.duration = duration;
    }

    /**
     * Retrieves the name of the class.
     *
     * @return The name of the class.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the class.
     *
     * @return The description of the class.
     */
    public String getDescrip() {
        return descrip;
    }

    /**
     * Retrieves the duration of the class.
     *
     * @return The duration of the class.
     */
    public Time getDuration() {
        return duration;
    }

    /**
     * Retrieves the scheduled date and time of the class.
     *
     * @return The scheduled date and time.
     */
    public Date getTime() {
        return time;
    }
}
