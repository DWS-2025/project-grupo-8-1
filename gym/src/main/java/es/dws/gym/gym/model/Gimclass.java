package es.dws.gym.gym.model;

import java.sql.Date;
import java.sql.Time;

public class Gimclass {
    private String name;
    private String descrip;
    private Date time;
    private Time duration;

    public Gimclass(String name, String descript, Date time, Time duration){
        this.name = name;
        this.descrip = descript;
        this.time = time;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescrip() {
        return descrip;
    }

    public Time getDuration() {
        return duration;
    }

    public Date getTime() {
        return time;
    }
}
