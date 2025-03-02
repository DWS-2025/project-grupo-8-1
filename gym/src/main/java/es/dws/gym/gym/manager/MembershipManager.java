package es.dws.gym.gym.manager;

import es.dws.gym.gym.model.Memberships;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class MembershipManager {

    // Lista de planes de membresía
    private List<Memberships> membershipPlans;

    public MembershipManager() {
        membershipPlans = new ArrayList<>();
        membershipPlans.add(new Memberships("Monthly", 29.99f, 1));
        membershipPlans.add(new Memberships("Quarterly", 79.99f, 2));
        membershipPlans.add(new Memberships("Anual", 249.99f, 3));
    }


    public List<Memberships> getAvailablePlans() {
        return membershipPlans;
    }


    public boolean subscribeUserToPlan(String userName, int typeMemberships) {
        for (Memberships plan : membershipPlans) {
            if (plan.gettypeMemberships() == typeMemberships) {
                System.out.println(userName + " has subscribed to the " + plan.getName() + " plan.");
                return true;
            }
        }
        return false;
    }

}
