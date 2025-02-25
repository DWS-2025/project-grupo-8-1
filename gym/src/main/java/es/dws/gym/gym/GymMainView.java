package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GymMainView {
    
    @GetMapping("/")
    public String index(@CookieValue(value = "login", defaultValue = "") String login, Model model){
        if(login.isEmpty()){
            model.addAttribute("user", false);
        }else{
            model.addAttribute("user", true);
            model.addAttribute("userName", login);
        }
        return "index";
    }
}
