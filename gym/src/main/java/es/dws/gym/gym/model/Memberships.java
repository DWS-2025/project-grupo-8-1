package es.dws.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Represents a gym membership with details such as ID, name, price, and membership type.

public class Memberships {

    // Unique identifier for the membership
    private Integer id;

    // Name of the membership plan
    private String name;

    // Price of the membership
    private float pay;

    // Type of membership (e.g., monthly, quarterly, annual)
    private Integer typeMemberships;

    // Constructor to initialize a membership.
    public Memberships(@JsonProperty("id") Integer id,
                       @JsonProperty("name") String name,
                       @JsonProperty("pay") float pay,
                       @JsonProperty("typeMemberships") Integer typeMemberships) {
        this.id = id;
        this.name = name;
        this.pay = pay;
        this.typeMemberships = typeMemberships;
    }

    // Retrieves the membership ID.

    public Integer getId() {
        return id;
    }


    //Retrieves the name of the membership.
    public String getName() {
        return name;
    }


    // Retrieves the price of the membership.
    public float getPay() {
        return pay;
    }


    //Retrieves the type of membership.
    public int getTypeMemberships() {
        return typeMemberships;
    }


    // Sets the membership ID.
    public void setId(Integer id) {
        this.id = id;
    }

    //Sets the name of the membership.
    public void setName(String name) {
        this.name = name;
    }


    //Sets the price of the membership.
    public void setPay(float pay) {
        this.pay = pay;
    }

    // Sets the type of membership.
    public void setTypeMemberships(int typeMemberships) {
        this.typeMemberships = typeMemberships;
    }
}
