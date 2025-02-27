package es.dws.gym.gym;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dws.gym.gym.manager.UserManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UserWebControl {
    private UserManager userManager;

    public UserWebControl(UserManager userManager){
        this.userManager = userManager;
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);
        return "login";
    }

    @GetMapping("/register")
    public String register(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);
        return "register";
    }

    @PostMapping("/register")
    public String newregister(@RequestParam String userName, @RequestParam String firstname, @RequestParam String secondName, @RequestParam String telephone,@RequestParam String mail, @RequestParam String address, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if(userManager.isUser(userName)){
            model.addAttribute("error", "Existing user name, please try with a different name");
            model.addAttribute("error_redirect", "/register");
            return "error";
        }
        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "nn");
            model.addAttribute("error_redirect", "/register");
            return "error";
        }
        userManager.addUser(userName, firstname, secondName, telephone, mail, address, password);
        return "redirect:/login";
    }
    
    @PostMapping("/login")
    public String loadLogin(@RequestParam String userName, @RequestParam String password, Model model, HttpServletResponse response) {
        if(!userManager.login(userName, password)){
            model.addAttribute("error", "the user or password is not correct");
            model.addAttribute("error_redirect", "/login");
            return "error";
        }
        Cookie cookieUser = new Cookie("login", userName);
        response.addCookie(cookieUser);
        return "redirect:/";
    }
    
    @GetMapping("/home")
    public String homeUser(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("user", true);
        model.addAttribute("userName", login);
        List<String> dataUser = userManager.getUserList(login);
        model.addAttribute("firstName", dataUser.get(0));
        model.addAttribute("sureName", dataUser.get(1));
        model.addAttribute("telephone", dataUser.get(2));
        model.addAttribute("mail", dataUser.get(3));
        model.addAttribute("address", dataUser.get(4));
        return "homeUser";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("login", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
    
    @GetMapping("/newpassword")
    public String newpassword(@CookieValue(value = "login", defaultValue = "") String login) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        return "newpassword";
    }
    
    @PostMapping("/newpassword")
    public String changePassword(@CookieValue(value = "login", defaultValue = "") String login ,@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }        
        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/newpassword");
            return "error";
        }
        userManager.setPassword(login, password);
        return "redirect:/home";
    }
    
}
