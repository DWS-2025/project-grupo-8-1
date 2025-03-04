package es.dws.gym.gym;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dws.gym.gym.manager.GimClassManager;
import es.dws.gym.gym.manager.UserManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class serves as the controller for handling user-related operations
 * such as login, registration, updating passwords, and managing user sessions.
 * It interacts with the UserManager to manage user data and operations.
 */
@Controller
public class UserWebControl {

    // Instance variable for managing user operations
    private UserManager userManager;
    private GimClassManager gimClassManager;

    // A flag to track whether the password was successfully changed
    private boolean trueMessage;


    //Constructor to initialize the UserWebControl class with a UserManager instance.
    public UserWebControl(UserManager userManager, GimClassManager gimClassManager){
        this.userManager = userManager;
        this.gimClassManager = gimClassManager;
        this.trueMessage = false;
    }


    //Handles GET requests to the login page. If the user is already logged in,they are redirected to the homepage.
    @GetMapping("/login")
    public String login(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);
        return "login";
    }


    //Handles GET requests to the registration page. If the user is already logged in,they are redirected to the homepage.
    @GetMapping("/register")
    public String register(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);
        return "register";
    }


    // Handles POST requests to register a new user. It checks if the username is available and whether the password and confirmation match before adding the new user.
    @PostMapping("/register")
    public String newregister(@RequestParam String userName, @RequestParam String firstname, @RequestParam String secondName, @RequestParam String telephone,@RequestParam String mail, @RequestParam String address, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if(userManager.isUser(userName)){
            model.addAttribute("error", "Existing user name, please try with a different name");
            model.addAttribute("error_redirect", "/register");
            return "error";  // Return error if username is already taken
        }

        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/register");
            return "error";
        }
        userManager.addUser(userName, firstname, secondName, telephone, mail, address, password);
        return "redirect:/login";
    }


    // Handles POST requests to log in a user. It checks if the provided username and password are valid and sets a login cookie if successful.
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

    //Handles GET requests to the home page for a logged-in user. It checks if the user is logged in and displays the user's data, such as name, email, and phone number.
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

        model.addAttribute("password", isViewTrueMenssage());
        model.addAttribute("message", "password changed correctly");
        model.addAttribute("classGim", gimClassManager.listGimClass());
        return "homeUser";  // Return the homepage view for the user
    }


    // Handles GET requests for logging out a user. It deletes the login cookie and redirects to the homepage.
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("login", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";  // Redirect to the homepage after logging out
    }


    //Handles GET requests to the password change page. If the user is not logged in,they will be redirected to the login page.
    @GetMapping("/newpassword")
    public String newpassword(@CookieValue(value = "login", defaultValue = "") String login) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        return "newpassword";
    }

    //Handles POST requests to change the user's password. It checks if the password and confirmation match before updating the password in the system.
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
        this.trueMessage = true;
        return "redirect:/home";
    }


    //A helper method to check if a password change success message should be displayed.
    private boolean isViewTrueMenssage(){
        if(this.trueMessage){
            trueMessage = false;
            return true;
        }
        return false;
    }
    
}