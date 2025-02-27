package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.dws.gym.gym.manager.ReviewManager;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ReviewWebControl {
    private ReviewManager reviewManager;

    public ReviewWebControl(ReviewManager reviewManager){
        this.reviewManager = reviewManager;
    }

    @GetMapping("/review")
    public String review(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            model.addAttribute("user", false);
        }else{
            model.addAttribute("user", true);
            model.addAttribute("userName", login);
        }
        model.addAttribute("reviews", reviewManager.listReview());
        return "review";
    }
    
    @GetMapping("/review/add")
    public String addReview(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/review";
        }
        model.addAttribute("user", true);
        model.addAttribute("userName", login);
        return "addreview";
    }

    @PostMapping("/review/add")
    public String createReview(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String contenReview) {
        reviewManager.addReview(login, contenReview);
        return "redirect:/review";
    }
    
    
}
