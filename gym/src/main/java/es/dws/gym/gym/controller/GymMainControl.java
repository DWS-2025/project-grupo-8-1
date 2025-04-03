package es.dws.gym.gym.controller;

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
public class GymMainControl {


    //This method handles GET requests to the root URL ("/").It checks the "login" cookie to determine if the user is logged in, and sets the "user" and "userName" attributes accordingly in the model.
    @GetMapping("/")
    public String index(@CookieValue(value = "login", defaultValue = "") String login, Model model){

        if(login.isEmpty()){
            model.addAttribute("user_login", false);
        }else{
            model.addAttribute("user_login", true);
            model.addAttribute("userName", login);
        }

        return "index";
    }
}
