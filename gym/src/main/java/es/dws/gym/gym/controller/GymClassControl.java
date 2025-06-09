package es.dws.gym.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.dws.gym.gym.service.GymClassService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// This class handles the gym class-related views and operations.
// It interacts with the GymClassService to manage gym class data and operations.
@Controller
public class GymClassControl {
    
    // Instance variable for managing gym class operations
    @Autowired
    private GymClassService gimClassService;

    // This method handles GET requests for the "/gymclass" page. It checks the login status of the user and passes the list of gym classes to the view for rendering.
    @GetMapping("/gymclass")
    public String GymClassMain(Model model) {

        model.addAttribute("GymClass", gimClassService.getListGymClass());
        return "ClassGym/ClassGym";
    }
    
    // This method handles GET requests for the "/gymclass/add" page.
    @GetMapping("/gymclass/add")
    public String GymClassAddForm(Model model) {
        return "ClassGym/AddClassGym";
    }
    
    // This method handles POST requests for adding a new gym class. It processes the submitted class details and adds the class using the GymClassService.
    @PostMapping("/gymclass/add")
    public String GymClassAdd(@RequestParam String name, 
                              @RequestParam String descript, 
                              @RequestParam String time, 
                              @RequestParam String duration, 
                              Model model) {

        if (name == null || name.isEmpty() || descript == null || descript.isEmpty() || 
            time == null || time.isEmpty() || duration == null || duration.isEmpty()) {
            model.addAttribute("error", "Error: All fields are required.");
            model.addAttribute("error_redirect", "/gymclass/add");
            return "error";
        }

        try {
            gimClassService.addClass(name, descript, time, duration);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Error: Invalid time or duration format.");
            model.addAttribute("error_redirect", "/gymclass/add");
            return "error";
        }

        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for the "/gymclass/{id}/edit" page. It checks if the user is logged in and if the class exists before rendering the edit form.
    @GetMapping("/gymclass/{id}/edit")
    public String GymClassEditForm(@PathVariable Long id, Model model) {
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: the class does not exist");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }

        model.addAttribute("GymClass", gimClassService.getGimClass(id));
        return "ClassGym/EditClassGym";
    }
    
    // This method handles POST requests for editing an existing gym class. It processes the submitted class details and updates the class using the GymClassService.
    @PostMapping("/gymclass/{id}/edit")
    public String GymClassEdit(@PathVariable Long id, 
                               @RequestParam String name, 
                               @RequestParam String descript, 
                               @RequestParam String time, 
                               @RequestParam String duration, 
                               Model model) {
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: The class does not exist.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }

        gimClassService.updateClass(id, name, descript, time, duration);
        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for the "/gymclass/{id}/delete" page.
    @GetMapping("/gymclass/{id}/delete")
    public String GymClassDeleteConfirm(@PathVariable Long id, Model model) {
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: the class does not exist");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }

        model.addAttribute("GymClass", gimClassService.getGimClass(id));
        return "ClassGym/are_your_sure_delete_class";
    }
    
    // This method handles POST requests for deleting a gym class. It checks if the class exists and if the action is confirmed before deleting the class using the GymClassService.
    @PostMapping("/gymclass/{id}/delete")
    public String GymClassDelete(@PathVariable Long id, @RequestParam(required = false) String action, Model model) {
        if (!"true".equals(action)) {
            return "redirect:/gymclass";
        }

            if (gimClassService.getGimClass(id) == null) {
                model.addAttribute("error", "Error: The class does not exist.");
                model.addAttribute("error_redirect", "/gymclass");
                return "error";
            }

            gimClassService.deleteClass(id);
        return "redirect:/gymclass";
    }
    
    // This method handles GET requests for toggling a user's participation in a gym class.
    @GetMapping("/gymclass/{id}/toggleUser")
    public String toggleUserInClass(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        if (gimClassService.getGimClass(id) == null) {
            model.addAttribute("error", "Error: The class does not exist.");
            model.addAttribute("error_redirect", "/gymclass");
            return "error";
        }

        gimClassService.toggleUserInClass(id, userId);
        return "redirect:/gymclass";
    }

    // This method handles GET requests for searching gym classes dynamically.
    @GetMapping("/gymclass/search")
    public String searchGymClasses(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) boolean myClasses,
                                   Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authentication != null
            && authentication.getPrincipal() instanceof String
            && authentication.getPrincipal().equals("anonymousUser");

        if (myClasses) {
            if (!isAnonymous) {
                String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
                model.addAttribute("GymClass", gimClassService.getGymClassesByUser(userId));
            } else {
                model.addAttribute("GymClass", null);
            }
        } else if (name != null && !name.isEmpty()) {
            model.addAttribute("GymClass", gimClassService.searchGymClassesByName(name));
        } else {
            model.addAttribute("GymClass", gimClassService.getListGymClass());
        }

        return "ClassGym/ClassGym";
    }
}