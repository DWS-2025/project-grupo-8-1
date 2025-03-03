package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class handles the main view for the gym application. It checks
 * the "login" cookie to determine if a user is logged in and passes the
 * appropriate information to the model for rendering the "index" view.
 */
@Controller
public class GymMainView {

    /**
     * This method handles GET requests to the root URL ("/").
     * It checks the "login" cookie to determine if the user is logged in,
     * and sets the "user" and "userName" attributes accordingly in the model.
     *
     * @param login The value of the "login" cookie (default is an empty string if not present).
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view to render (in this case, "index").
     */
    @GetMapping("/")
    public String index(@CookieValue(value = "login", defaultValue = "") String login, Model model){

        // If the "login" cookie is empty, the user is not logged in
        if(login.isEmpty()){
            model.addAttribute("user", false);  // Set "user" attribute as false in the model
        }else{
            model.addAttribute("user", true);   // Set "user" attribute as true in the model
            model.addAttribute("userName", login);  // Set "userName" attribute as the value of the "login" cookie
        }

        // Return the "index" view, which will be mapped to a corresponding template
        return "index";
    }
}
