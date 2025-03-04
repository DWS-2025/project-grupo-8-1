package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.dws.gym.gym.manager.ReviewManager;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// This class handles the review-related views and operations.
@Controller
public class ReviewWebControl {
    private ReviewManager reviewManager;

    //Constructor to inject the ReviewManager dependency.
    public ReviewWebControl(ReviewManager reviewManager){
        this.reviewManager = reviewManager;
    }

    // This method handles GET requests for the "/review" page.It checks the login status of the user and passes the list of reviews to the view for rendering.
    @GetMapping("/review")
    public String review(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If the "login" cookie is empty, the user is not logged in
        if(login.isEmpty()){
            model.addAttribute("user", false);
        }else{
            model.addAttribute("user", true);
            model.addAttribute("userName", login);
        }

        model.addAttribute("reviews", reviewManager.listReview());
        return "review";
    }

    //This method handles GET requests for the "/review/add" page.It checks if the user is logged in and redirects to the review page if not.
    @GetMapping("/review/add")
    public String addReview(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/review";
        }
        model.addAttribute("user", true);
        model.addAttribute("userName", login);
        return "addreview";
    }

    //This method handles POST requests for adding a new review.It processes the submitted review content and adds the review using the ReviewManager.
    @PostMapping("/review/add")
    public String createReview(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String contenReview) {

        reviewManager.addReview(login, contenReview);
        return "redirect:/review";
    }
}

