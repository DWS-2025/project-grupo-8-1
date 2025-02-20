package es.dws.gym.memberships;

public class memberships {
    private String name;
    private float pay;
    private int type_memberships;

    public memberships(String name, float pay, int type_memberships){
        this.name = name;
        this.pay = pay;
        this.type_memberships = type_memberships;
    }

    public String getName() {
        return name;
    }

    public float getPay() {
        return pay;
    }

    public int getType_memberships() {
        return type_memberships;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public void setType_memberships(int type_memberships) {
        this.type_memberships = type_memberships;
    }
}
