package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import es.dws.gym.gym.manager.MembershipManager;

@Controller
public class MembershipWebControl {

    private MembershipManager membresiaManager;

    // Constructor to inject the MembershipManager
    public MembershipWebControl(MembershipManager membresiaManager) {
        this.membresiaManager = membresiaManager;
    }

    // Display all membership plans. Checks if the user is logged in via cookies.
    @GetMapping("/memberships")
    public String showMemberships(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("user", false);
        } else {
            model.addAttribute("user", true);
            model.addAttribute("userName", login);
        }

        model.addAttribute("membershipPlans", membresiaManager.listMemberships());

        return "memberships";
    }

    // Redirect to the payment selection page for a specific membership plan
    @GetMapping("/memberships/{id}")
    public String showSelectionPay(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("id", id);
        return "selectionPay";
    }

    // Redirect to the subscription page for effective payment method
    @GetMapping("/memberships/{id}/efective")
    public String payEfective(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);
        return "subscribe";
    }

    // Redirect to the subscription page for card payment method
    @GetMapping("/memberships/{id}/card")
    public String payCard(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", true);
        return "subscribe";
    }

    // Post method to handle card payment
    @PostMapping("/memberships/{id}/card")
    public String postMethodName(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);
        return "subscribe";
    }
}



