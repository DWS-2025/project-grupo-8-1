package es.dws.gym.gym.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserModelAtributes {
    
    @ModelAttribute
    public void addAttribute(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null 
            && authentication.isAuthenticated()
            && !(authentication.getPrincipal() instanceof String 
                && authentication.getPrincipal().equals("anonymousUser"));
        model.addAttribute("user_login", isLoggedIn);

        String userName = isLoggedIn && authentication.getPrincipal() instanceof UserDetails
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : null;

        if (isLoggedIn) {
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", null);
        }

        boolean isAdmin = false;
        if (isLoggedIn && authentication.getAuthorities() != null) {
            isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);
    }
}
