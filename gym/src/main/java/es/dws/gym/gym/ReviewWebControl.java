package es.dws.gym.gym;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.dws.gym.gym.manager.ReviewManager;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class handles the review-related views and operations.
 * It allows users to view, add, and create reviews.
 */
@Controller
public class ReviewWebControl {
    private ReviewManager reviewManager;

    /**
     * Constructor to inject the ReviewManager dependency.
     *
     * @param reviewManager The ReviewManager used to handle review operations.
     */
    public ReviewWebControl(ReviewManager reviewManager){
        this.reviewManager = reviewManager;
    }

    /**
     * This method handles GET requests for the "/review" page.
     * It checks the login status of the user and passes the list of reviews
     * to the view for rendering.
     *
     * @param login The value of the "login" cookie (default is an empty string if not present).
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view to render (in this case, "review").
     */
    @GetMapping("/review")
    public String review(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If the "login" cookie is empty, the user is not logged in
        if(login.isEmpty()){
            model.addAttribute("user", false);  // Set "user" attribute as false in the model
        }else{
            model.addAttribute("user", true);   // Set "user" attribute as true in the model
            model.addAttribute("userName", login);  // Set "userName" attribute as the value of the "login" cookie
        }

        // Add the list of reviews to the model
        model.addAttribute("reviews", reviewManager.listReview());
        return "review";  // Return the "review" view, which will be mapped to the corresponding template
    }

    /**
     * This method handles GET requests for the "/review/add" page.
     * It checks if the user is logged in and redirects to the review page if not.
     *
     * @param login The value of the "login" cookie (default is an empty string if not present).
     * @param model The model to add attributes for rendering the view.
     * @return The name of the view to render (in this case, "addreview").
     */
    @GetMapping("/review/add")
    public String addReview(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If the user is not logged in, redirect them to the review page
        if(login.isEmpty()){
            return "redirect:/review";
        }

        // Add user login status and username to the model
        model.addAttribute("user", true);
        model.addAttribute("userName", login);
        return "addreview";  // Return the "addreview" view to allow the user to add a review
    }

    /**
     * This method handles POST requests for adding a new review.
     * It processes the submitted review content and adds the review using the ReviewManager.
     *
     * @param login The value of the "login" cookie (default is an empty string if not present).
     * @param contenReview The content of the review submitted by the user.
     * @return A redirect to the "/review" page to display the updated list of reviews.
     */
    @PostMapping("/review/add")
    public String createReview(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String contenReview) {
        // Add the new review using the ReviewManager
        reviewManager.addReview(login, contenReview);
        return "redirect:/review";  // Redirect to the review page to display the new review
    }
}

