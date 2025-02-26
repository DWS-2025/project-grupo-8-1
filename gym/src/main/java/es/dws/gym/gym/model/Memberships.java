package es.dws.gym.gym.model;

public class Memberships {
    private String name;
    private float pay;
    private int typeMemberships;

    public Memberships(String name, float pay, int typeMemberships){
        this.name = name;
        this.pay = pay;
        this.typeMemberships = typeMemberships;
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
