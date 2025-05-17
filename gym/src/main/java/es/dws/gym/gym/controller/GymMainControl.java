package es.dws.gym.gym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class handles the main view for the gym application. It checks
 * the authentication context to determine if a user is logged in and passes the
 * appropriate information to the model for rendering the "index" view.
 */
@Controller
public class GymMainControl {

    // This method handles GET requests for the root URL ("/"). It checks the login status of the user
    @GetMapping("/")
    public String index(Model model) {
    
        return "index";
    }
}
