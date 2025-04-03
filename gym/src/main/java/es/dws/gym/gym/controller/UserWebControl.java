package es.dws.gym.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;

import java.sql.SQLException;
import java.io.IOException;

import es.dws.gym.gym.model.User;
import es.dws.gym.gym.service.ImageService;
import es.dws.gym.gym.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


/**
 * This class serves as the controller for handling user-related operations
 * such as login, registration, updating passwords, and managing user sessions.
 * It interacts with the UserService to manage user data and operations.
 */
@Controller
public class UserWebControl {

    // Instance variable for managing user operations
    @Autowired
    private UserService userService;
    // Instance variable for managing image uploads and validations
    @Autowired
    private ImageService imageService;

    //Handles GET requests to the login page. If the user is already logged in,they are redirected to the homepage.
    @GetMapping("/login")
    public String login(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user_login", false);
        return "user/login";
    }


    //Handles GET requests to the registration page. If the user is already logged in,they are redirected to the homepage.
    @GetMapping("/register")
    public String register(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(!login.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("user_login", false);
        return "user/register";
    }


    // Handles POST requests to register a new user. It checks if the username is available and whether the password and confirmation match before adding the new user.
    @PostMapping("/register")
    public String newregister(@RequestParam String userName, @RequestParam String firstname, @RequestParam String secondName, @RequestParam String telephone, @RequestParam String mail, @RequestParam String address, @RequestParam String password, @RequestParam String confirmPassword, @RequestParam(required = false) MultipartFile imageUpload, Model model) throws Exception {
        if(userService.isUser(userName)){
            model.addAttribute("error", "Existing user name, please try with a different name");
            model.addAttribute("error_redirect", "/register");
            return "error";  // Return error if username is already taken
        }

        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/register");
            return "error";
        }
        
        MultipartFile imageFile = (imageUpload != null && !imageUpload.isEmpty()) ? imageUpload : null;
        if(imageFile != null && !imageService.validateImage(imageFile, "/register", model)){
            return "error";
        }
        
        userService.addUser(userName, firstname, secondName, telephone, mail, address, password, imageFile);
        return "redirect:/login";
    }


    // Handles POST requests to log in a user. It checks if the provided username and password are valid and sets a login cookie if successful.
    @PostMapping("/login")
    public String loadLogin(@RequestParam String userName, @RequestParam String password, Model model, HttpServletResponse response) {
        User user = userService.getUser(userName);
        if(user == null || !user.getPassword().equals(password)){
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
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("user", userService.getUser(login));
        return "user/homeUser";  // Return the homepage view for the user
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
    @GetMapping("/user/newpassword")
    public String newpassword(@CookieValue(value = "login", defaultValue = "") String login) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        return "user/newpassword";
    }

    //Handles POST requests to change the user's password. It checks if the password and confirmation match before updating the password in the system.
    @PostMapping("/user/newpassword")
    public String changePassword(@CookieValue(value = "login", defaultValue = "") String login ,@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }

        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/newpassword");
            return "error";
        }
        
        userService.setPassword(login, password);
        return "redirect:/home";
    }
    
    //Handles GET requests to the user edit page. If the user is not logged in,they will be redirected to the login page.
    @GetMapping("/user/edit")
    public String editUser(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/";
        }
        User user = userService.getUser(login);
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("User", user);
        return "user/editUser";
    }

    //Handles POST requests to update the user's information. It checks if the image is valid and updates the user's data in the system.
    @PostMapping("/user/edit")
    public String uploadUser(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String firstname, @RequestParam String secondName, @RequestParam String telephone,@RequestParam String mail, @RequestParam String address, @RequestParam MultipartFile imageUpload, Model model) throws Exception {
        if(!imageService.validateImage(imageUpload, "/edit", model)){
            return "error";
        }
        userService.editUser(login, firstname, secondName, telephone, mail, address, imageUpload);
        return "redirect:/home";
    }

    //Handles GET requests to the user delete confirmation page. If the user is not logged in,they will be redirected to the login page.
    @GetMapping("/user/delete")
    public String questionDeleteUser(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        return "user/are_your_sure_delete_user";
    }

    //Handles GET requests to delete the user. If the user is not logged in,they will be redirected to the login page.
    @GetMapping("/user/delete/true")
    public String deleteUser(@CookieValue(value = "login", defaultValue = "") String login) {
        User user = userService.getUser(login);
        userService.removeUser(user);
        return "redirect:/logout";
    }
    //Handles GET requests to delete the user without confirmation. If the user is not logged in,they will be redirected to the login page.
    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadUserImage(@PathVariable String id) throws SQLException, IOException {
        User user = userService.getUser(id);
        if (user != null && user.getImageUser() != null) {
            byte[] imageBytes = user.getImageUser().getBytes(1, (int) user.getImageUser().length());
            // Siempre se devolverá como JPG
            String contentType = "image/jpeg";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .contentLength(imageBytes.length)
                    .body(new org.springframework.core.io.ByteArrayResource(imageBytes));
        } else {
            Resource defaultFile = new ClassPathResource("static/images/default.jpg");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(defaultFile.contentLength())
                    .body(defaultFile);
        }
    }
}