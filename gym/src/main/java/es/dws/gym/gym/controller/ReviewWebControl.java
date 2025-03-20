package es.dws.gym.gym.controller;
import es.dws.gym.gym.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// This class handles the review-related views and operations.
@Controller
public class ReviewWebControl {

    @Autowired
    private ReviewService reviewService;
    
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

        model.addAttribute("reviews", reviewService.getAllReviews());
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

        reviewService.addReview(login, contenReview);;
        return "redirect:/review";
    }
}

