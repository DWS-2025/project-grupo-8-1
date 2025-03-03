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
            model.addAttribute("user", false);  // If the user is not authenticated
        } else {
            model.addAttribute("user", true);
            model.addAttribute("userName", login);  // Display the user's name if authenticated
        }

        // Add the list of membership plans to the model
        model.addAttribute("membershipPlans", membresiaManager.listMemberships());

        return "memberships";
    }

    // Redirect to the payment selection page for a specific membership plan
    @GetMapping("/memberships/{id}")
    public String showSelectionPay(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";  // Redirect to login page if user is not authenticated
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";  // Redirect to memberships page if membership plan doesn't exist
        }
        model.addAttribute("userName", login);
        model.addAttribute("id", id);
        return "selectionPay";  // Show the payment selection page
    }

    // Redirect to the subscription page for effective payment method
    @GetMapping("/memberships/{id}/efective")
    public String payEfective(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";  // Redirect to login page if user is not authenticated
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";  // Redirect to memberships page if membership plan doesn't exist
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);  // Specify that it's an effective payment method
        return "subscribe";  // Show the subscription page
    }

    // Redirect to the subscription page for card payment method
    @GetMapping("/memberships/{id}/card")
    public String payCard(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";  // Redirect to login page if user is not authenticated
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";  // Redirect to memberships page if membership plan doesn't exist
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", true);  // Specify that it's a card payment method
        return "subscribe";  // Show the subscription page
    }

    // Post method to handle card payment
    @PostMapping("/memberships/{id}/card")
    public String postMethodName(@PathVariable Integer id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            return "redirect:/login";  // Redirect to login page if user is not authenticated
        }
        if (!membresiaManager.isExistKey(id)) {
            return "redirect:/memberships";  // Redirect to memberships page if membership plan doesn't exist
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);  // Specify that it's not a card payment method
        return "subscribe";  // Show the subscription page
    }
}



