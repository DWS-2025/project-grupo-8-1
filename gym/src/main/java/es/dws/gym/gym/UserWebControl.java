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

/**
 * This class serves as the controller for handling user-related operations
 * such as login, registration, updating passwords, and managing user sessions.
 * It interacts with the UserManager to manage user data and operations.
 */
@Controller
public class UserWebControl {

    // Instance variable for managing user operations
    private UserManager userManager;

    // A flag to track whether the password was successfully changed
    private boolean trueMessage;

    /**
     * Constructor to initialize the UserWebControl class with a UserManager instance.
     *
     * @param userManager The UserManager object to handle user-related operations.
     */
    public UserWebControl(UserManager userManager){
        this.userManager = userManager;
        this.trueMessage = false;
    }

    /**
     * Handles GET requests to the login page. If the user is already logged in,
     * they are redirected to the homepage.
     *
     * @param login The login cookie value that indicates if the user is logged in.
     * @param model The Model object to add attributes to the view.
     * @return The login page view or a redirect to the homepage if already logged in.
     */
    @GetMapping("/login")
    public String login(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If user is logged in, redirect to homepage
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);  // Add attribute indicating user is not logged in
        return "login";  // Return the login view
    }

    /**
     * Handles GET requests to the registration page. If the user is already logged in,
     * they are redirected to the homepage.
     *
     * @param login The login cookie value that indicates if the user is logged in.
     * @param model The Model object to add attributes to the view.
     * @return The registration page view or a redirect to the homepage if already logged in.
     */
    @GetMapping("/register")
    public String register(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If user is logged in, redirect to homepage
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user", false);  // Add attribute indicating user is not logged in
        return "register";  // Return the registration view
    }

    /**
     * Handles POST requests to register a new user. It checks if the username is available
     * and whether the password and confirmation match before adding the new user.
     *
     * @param userName The desired username for the new user.
     * @param firstname The first name of the new user.
     * @param secondName The second name (last name) of the new user.
     * @param telephone The telephone number of the new user.
     * @param mail The email address of the new user.
     * @param address The address of the new user.
     * @param password The password for the new user.
     * @param confirmPassword The password confirmation field.
     * @param model The Model object to add attributes to the view.
     * @return A redirect to the login page if registration is successful or an error view if registration fails.
     */
    @PostMapping("/register")
    public String newregister(@RequestParam String userName, @RequestParam String firstname, @RequestParam String secondName, @RequestParam String telephone,@RequestParam String mail, @RequestParam String address, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        // Check if the username already exists
        if(userManager.isUser(userName)){
            model.addAttribute("error", "Existing user name, please try with a different name");
            model.addAttribute("error_redirect", "/register");
            return "error";  // Return error if username is already taken
        }
        // Check if passwords match
        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/register");
            return "error";  // Return error if passwords don't match
        }
        // Register the new user
        userManager.addUser(userName, firstname, secondName, telephone, mail, address, password);
        return "redirect:/login";  // Redirect to login page after successful registration
    }

    /**
     * Handles POST requests to log in a user. It checks if the provided username and password
     * are valid and sets a login cookie if successful.
     *
     * @param userName The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @param model The Model object to add attributes to the view.
     * @param response The HttpServletResponse object to add the login cookie.
     * @return A redirect to the homepage if login is successful or an error view if login fails.
     */
    @PostMapping("/login")
    public String loadLogin(@RequestParam String userName, @RequestParam String password, Model model, HttpServletResponse response) {
        // Validate the username and password
        if(!userManager.login(userName, password)){
            model.addAttribute("error", "the user or password is not correct");
            model.addAttribute("error_redirect", "/login");
            return "error";  // Return error if login fails
        }
        // Set a login cookie for the authenticated user
        Cookie cookieUser = new Cookie("login", userName);
        response.addCookie(cookieUser);
        return "redirect:/";  // Redirect to the homepage after successful login
    }

    /**
     * Handles GET requests to the home page for a logged-in user. It checks if the user is logged in
     * and displays the user's data, such as name, email, and phone number.
     *
     * @param login The login cookie value that indicates if the user is logged in.
     * @param model The Model object to add attributes to the view.
     * @return The homepage view for logged-in users or a redirect to the login page if not logged in.
     */
    @GetMapping("/home")
    public String homeUser(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If the user is not logged in, redirect to the login page
        if(login.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("user", true);  // Add attribute indicating the user is logged in
        model.addAttribute("userName", login);  // Set the logged-in user's name

        // Retrieve the user's details from the UserManager
        List<String> dataUser = userManager.getUserList(login);
        model.addAttribute("firstName", dataUser.get(0));
        model.addAttribute("sureName", dataUser.get(1));
        model.addAttribute("telephone", dataUser.get(2));
        model.addAttribute("mail", dataUser.get(3));
        model.addAttribute("address", dataUser.get(4));

        // Display a message about password change if applicable
        model.addAttribute("password", isViewTrueMenssage());
        model.addAttribute("message", "password changed correctly");
        return "homeUser";  // Return the homepage view for the user
    }

    /**
     * Handles GET requests for logging out a user. It deletes the login cookie and redirects to the homepage.
     *
     * @param response The HttpServletResponse object to remove the login cookie.
     * @return A redirect to the homepage after logging out.
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Remove the login cookie by setting its max age to 0
        Cookie cookie = new Cookie("login", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";  // Redirect to the homepage after logging out
    }

    /**
     * Handles GET requests to the password change page. If the user is not logged in,
     * they will be redirected to the login page.
     *
     * @param login The login cookie value that indicates if the user is logged in.
     * @return The new password page or a redirect to the login page if not logged in.
     */
    @GetMapping("/newpassword")
    public String newpassword(@CookieValue(value = "login", defaultValue = "") String login) {
        // If the user is not logged in, redirect to the login page
        if(login.isEmpty()){
            return "redirect:/login";
        }
        return "newpassword";  // Return the password change page
    }

    /**
     * Handles POST requests to change the user's password. It checks if the password and
     * confirmation match before updating the password in the system.
     *
     * @param login The login cookie value that indicates if the user is logged in.
     * @param password The new password the user wants to set.
     * @param confirmPassword The confirmation of the new password.
     * @param model The Model object to add attributes to the view.
     * @return A redirect to the homepage after successful password change or an error view if passwords don't match.
     */
    @PostMapping("/newpassword")
    public String changePassword(@CookieValue(value = "login", defaultValue = "") String login ,@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        // If the user is not logged in, redirect to the login page
        if(login.isEmpty()){
            return "redirect:/login";
        }
        // Check if passwords match
        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/newpassword");
            return "error";  // Return error if passwords don't match
        }
        // Update the user's password
        userManager.setPassword(login, password);
        this.trueMessage = true;  // Set flag to show password change success message
        return "redirect:/home";  // Redirect to the home page after password change
    }

    /**
     * A helper method to check if a password change success message should be displayed.
     *
     * @return true if the password change success message should be shown, otherwise false.
     */
    private boolean isViewTrueMenssage(){
        if(this.trueMessage){
            trueMessage = false;
            return true;
        }
        return false;
    }
}