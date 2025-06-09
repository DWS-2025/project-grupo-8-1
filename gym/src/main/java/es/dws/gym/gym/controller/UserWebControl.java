package es.dws.gym.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import es.dws.gym.gym.dto.UserDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
    public String login() {
        return "user/login";
    }

    //Handles POST requests to authenticate the user. If the authentication fails,an error message is displayed.
    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("error", "The user or password not found");
        model.addAttribute("error_redirect", "/login");
        return "error";  // Return error if username is already taken
    }
    
    //Handles GET requests to the registration page. If the user is already logged in,they are redirected to the homepage.
    @GetMapping("/register")
    public String register(Model model) {
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
        
        UserDTO newUser = new UserDTO(userName, firstname, secondName, telephone, mail, address, password, null, "USER");
        userService.addUser(newUser.id(), newUser.firstName(), newUser.sureName(), newUser.telephone(), newUser.mail(), newUser.address(), newUser.password(), imageFile, newUser.rol());
        return "redirect:/login";
    }

    //Handles GET requests to the home page for a logged-in user. It checks if the user is logged in and displays the user's data, such as name, email, and phone number.
    @GetMapping("/home")
    public String homeUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        model.addAttribute("user", userService.getUser(userId));
        return "user/homeUser";
    }

    //Handles GET requests to the password change page.
    @GetMapping("/user/newpassword")
    public String newpassword() {
        return "user/newpassword";
    }

    //Handles POST requests to change the user's password. It checks if the password and confirmation match before updating the password in the system.
    @PostMapping("/user/newpassword")
    public String changePassword(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/newpassword");
            return "error";
        }

        userService.setPassword(userId, password);
        return "redirect:/home";
    }
    
    //Handles GET requests to the user edit page.
    @GetMapping("/user/edit")
    public String editUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        User user = userService.getUser(userId);
        model.addAttribute("User", user);
        return "user/editUser";
    }

    //Handles POST requests to update the user's information. It checks if the image is valid and updates the user's data in the system.
    @PostMapping("/user/edit")
    public String uploadUser(
            @RequestParam String firstname,
            @RequestParam String secondName,
            @RequestParam String telephone,
            @RequestParam String mail,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile imageUpload,
            Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        // Permitir imagen null o vacía
        MultipartFile imageFile = (imageUpload != null && !imageUpload.isEmpty()) ? imageUpload : null;
        if (imageFile != null && !imageService.validateImage(imageFile, "/edit", model)) {
            return "error";
        }
        userService.editUser(userId, firstname, secondName, telephone, mail, address, imageFile);
        return "redirect:/home";
    }

    //Handles GET requests to the user delete confirmation page.
    @GetMapping("/user/delete")
    public String questionDeleteUser(Model model) {
        return "user/are_your_sure_delete_user";
    }

    //Handles POST requests to delete the user. If the action is confirmed, the user is removed from the system and redirected to the logout page.
    @PostMapping("/user/delete/true")
    public String deleteUser(@RequestParam(required = false) String action, Model model) {
        if (!"true".equals(action)) {
            // If the action is not confirmed, redirect to home
            return "redirect:/home";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();

        User user = userService.getUser(userId);
        userService.removeUser(user);
        return "redirect:/logout";
    }
    
    //Handles GET requests to delete the user without confirmation.
    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadUserImage(@PathVariable String id) throws SQLException, IOException {
        User user = userService.getUser(id);
        if (user != null && user.getImageUser() != null) {
            byte[] imageBytes = user.getImageUser().getBytes(1, (int) user.getImageUser().length());
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