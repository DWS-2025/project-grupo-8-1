package es.dws.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Memberships {
    private Integer id;
    private String name;
    private float pay;
    private Integer typeMemberships;

    public Memberships(@JsonProperty("id") Integer id,@JsonProperty("name") String name,@JsonProperty("pay") float pay,@JsonProperty("typeMemberships") Integer typeMemberships){
        this.id = id;
        this.name = name;
        this.pay = pay;
        this.typeMemberships = typeMemberships;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPay() {
        return pay;
    }

    public int gettypeMemberships() {
        return typeMemberships;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public void settypeMemberships(int typeMemberships) {
        this.typeMemberships = typeMemberships;
    }
}
