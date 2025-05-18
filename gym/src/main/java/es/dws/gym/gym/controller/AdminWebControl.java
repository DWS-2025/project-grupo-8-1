package es.dws.gym.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import es.dws.gym.gym.model.User;
import es.dws.gym.gym.service.ImageService;
import es.dws.gym.gym.service.UserService;

@Controller
public class AdminWebControl {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/listusers")
    public String adminListUser(Model model){
        model.addAttribute("Users", userService.getAllUsers());
        return "admin/listusers";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String userEditAdmin(@PathVariable String id, Model model) {
        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        model.addAttribute("User", userService.getUser(id));
        return "admin/editUserAdmin";
    }

    @PostMapping("/admin/user/edit/{id}")
    public String uploadUserAdmin(
            @PathVariable String id,
            @RequestParam String firstname,
            @RequestParam String secondName,
            @RequestParam String telephone,
            @RequestParam String mail,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile imageUpload,
            Model model) throws Exception {

        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        MultipartFile imageFile = (imageUpload != null && !imageUpload.isEmpty()) ? imageUpload : null;
        if (imageFile != null && !imageService.validateImage(imageFile, "/admin/user/edit/" + id, model)) {
            return "error";
        }
        userService.editUser(id, firstname, secondName, telephone, mail, address, imageFile);
        return "redirect:/admin/listusers";
    }

    @GetMapping("/admin/user/newpassword/{id}")
    public String newPasswordAdmin(@PathVariable String id, Model model) {
        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        model.addAttribute("id", id);
        return "admin/newpasswordAdmin";
    }

    @PostMapping("/admin/user/newpassword/{id}")
    public String changePasswordAdmin(
            @PathVariable String id,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "WARNING: passwords do not match");
            model.addAttribute("error_redirect", "/admin/user/newpassword/" + id);
            return "error";
        }

        userService.setPassword(id, password);
        return "redirect:/admin/listusers";
    }
    
    @GetMapping("/admin/user/delete/{id}")
    public String questionDeleteUserAdmin(@PathVariable String id, Model model) {
        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        model.addAttribute("User", userService.getUser(id));
        return "admin/are_your_sure_delete_user_admin";
    }
    
    @GetMapping("/admin/user/delete/{id}/true")
    public String deleteUser(@PathVariable String id, Model model) {
        if (!userService.isUser(id)) {
            model.addAttribute("error", "User not found");
            model.addAttribute("error_redirect", "/admin/listusers");
            return "error";
        }
        User user = userService.getUser(id);
        userService.removeUser(user);
        return "redirect:/admin/listusers";
    }
}
