package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dws.gym.gym.manager.MembershipManager;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class MembershipWebControl {

    private MembershipManager membresiaManager;

    public MembershipWebControl(MembershipManager membresiaManager) {
        this.membresiaManager = membresiaManager;
    }

    @GetMapping("/memberships")
    public String showMemberships(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("user", false);  // Si el usuario no está autenticado
        } else {
            model.addAttribute("user", true);
            model.addAttribute("userName", login);  // Mostrar el nombre del usuario si está autenticado
        }

        model.addAttribute("membershipPlans", membresiaManager.listMemberships()); //Trae todos los objetos membresias de la base de datos json

        return "memberships";
    }

    @GetMapping("/memberships/{id}")
    public String showSelectionPay(@PathVariable Integer id ,@CookieValue(value = "login", defaultValue = "") String login, Model model){
        if(login.isEmpty()){
            return "redirect:/login";
        }
        if(!membresiaManager.isExistKey(id)){
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("id", id);
        return "selectionPay";
    }

    @GetMapping("/memberships/{id}/efective")
    public String payEfective(@PathVariable Integer id ,@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        if(!membresiaManager.isExistKey(id)){
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);
        return "subscribe";
    }
    
    @GetMapping("/memberships/{id}/card")
    public String payCard(@PathVariable Integer id ,@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        if(!membresiaManager.isExistKey(id)){
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", true);
        return "subscribe";
    }

    @PostMapping("/memberships/{id}/card")
    public String postMethodName(@PathVariable Integer id ,@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        if(!membresiaManager.isExistKey(id)){
            return "redirect:/memberships";
        }
        model.addAttribute("userName", login);
        model.addAttribute("card", false);
        return "subscribe";
    }
    
    
/*
    @PostMapping("/memberships/subscribe")
    public String subscribeMembership(@CookieValue(value = "login", defaultValue = "") String login,
                                      @RequestParam String membershipType, Model model, HttpServletResponse response) {
        if (login.isEmpty()) {
            return "redirect:/login";  // Si no está autenticado, redirige al login
        }

        // Conversion
        int membershipTypeInt = Integer.parseInt(membershipType);

        // Manejo de la suscripción
        boolean subscriptionSuccess = membresiaManager.subscribeUserToPlan(login, membershipTypeInt);

        if (subscriptionSuccess) {
            model.addAttribute("subscriptionMessage", "You have successfully subscribed to the plan.");
            return "subscriptionSuccess"; // Redirige cuando hay éxito en la suscripción
        } else {
            model.addAttribute("error", "Something went wrong with the subscription.");
            return "error"; // Mostrar error en caso de fallo
        }
    }
        */
}



