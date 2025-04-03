package es.dws.gym.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.dws.gym.gym.service.GymClassService;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


// This class handles the gym class-related views and operations.
// It interacts with the GymClassService to manage gym class data and operations.
@Controller
public class GymClassControl {
    
    // Instance variable for managing gym class operations
    @Autowired
    private GymClassService gimClassService;

    // This method handles GET requests for the "/gymclass" page. It checks the login status of the user and passes the list of gym classes to the view for rendering.
    @GetMapping("/gymclass")
    public String GymClassMain(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if (login.isEmpty()){
            model.addAttribute("user_login", false);
        }else{
            model.addAttribute("user_login", true);
            model.addAttribute("userName", login);
        }
        model.addAttribute("GymClass", gimClassService.getListGymClass());
        return "ClassGym/ClassGym";
    }
    
    // This method handles GET requests for the "/gymclass/add" page. It checks if the user is logged in and redirects to the gym class page if not.
    @GetMapping("/gymclass/add")
    public String GymClassAddForm(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/gymclass";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        return "ClassGym/AddClassGym";
    }
    
    // This method handles POST requests for adding a new gym class. It processes the submitted class details and adds the class using the GymClassService.
    @PostMapping("/gymclass/add")
    public String GymClassAdd(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String name, @RequestParam String descript, @RequestParam String time, @RequestParam String duration, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("error", "Error: You must be logged in to add a class.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        gimClassService.addClass(name, descript, time, duration);
        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for the "/gymclass/{id}/edit" page. It checks if the user is logged in and if the class exists before rendering the edit form.
    @GetMapping("/gymclass/{id}/edit")
    public String GymClassEditForm(@CookieValue(value = "login", defaultValue = "") String login, @PathVariable Long id, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("user_login", false);
            return "redirect:/gymclass";
        }
        if(gimClassService.getGimClass(id) == null){
            model.addAttribute("error", "Error: the class does not exist");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("GymClass", gimClassService.getGimClass(id));
        return "ClassGym/EditClassGym";
    }
    
    // This method handles POST requests for editing an existing gym class. It processes the submitted class details and updates the class using the GymClassService.
    @PostMapping("/gymclass/{id}/edit")
    public String GymClassEdit(@CookieValue(value = "login", defaultValue = "") String login, @PathVariable Long id, @RequestParam String name, @RequestParam String descript, @RequestParam String time, @RequestParam String duration, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("error", "Error: You must be logged in to edit a class.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: The class does not exist.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        gimClassService.updateClass(id, name, descript, time, duration);
        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for the "/gymclass/{id}/delete" page. It checks if the user is logged in and if the class exists before confirming deletion.
    @GetMapping("/gymclass/{id}/delete")
    public String GymClassDeleteConfirm(@CookieValue(value = "login", defaultValue = "") String login, @PathVariable Long id, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("user_login", false);
            return "redirect:/gymclass";
        }
        if(gimClassService.getGimClass(id) == null){
            model.addAttribute("error", "Error: the class does not exist");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("GymClass", gimClassService.getGimClass(id));
        return "ClassGym/are_your_sure_delete_class";
    }
    
    // This method handles POST requests for deleting a gym class. It checks if the user is logged in and if the class exists before performing the deletion.
    @GetMapping("/gymclass/{id}/delete/{accion}")
    public String GymClassDelete(@CookieValue(value = "login", defaultValue = "") String login, @PathVariable Long id, @PathVariable String accion, Model model) {
        if(accion.equals("true")){
            if (login.isEmpty()) {
                model.addAttribute("error", "Error: You must be logged in to delete a class.");
                model.addAttribute("error_redirect", "/gymclass");
                return "error";
            }
            if (gimClassService.getGimClass(id) == null) {
                model.addAttribute("error", "Error: The class does not exist.");
                model.addAttribute("error_redirect", "/gymclass");
                return "error";
            }
            gimClassService.deleteClass(id);
        }
        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for toggling a user's participation in a gym class. It checks if the user is logged in and if the class exists before performing the toggle.
    @GetMapping("/gymclass/{id}/toggleUser")
    public String toggleUserInClass(@CookieValue(value = "login", defaultValue = "") String login, @PathVariable Long id, Model model) {
        if (login.isEmpty()) {
            model.addAttribute("error", "Error: You must be logged in to join or leave a class.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: The class does not exist.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }
        gimClassService.toggleUserInClass(id, login);
        return "redirect:/gymclass";
    }
}
