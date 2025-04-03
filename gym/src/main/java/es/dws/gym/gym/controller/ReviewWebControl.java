package es.dws.gym.gym.controller;
import es.dws.gym.gym.model.Review;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.service.ReviewService;
import es.dws.gym.gym.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




// This class handles the review-related views and operations.
@Controller
public class ReviewWebControl {

    // Instance variable for managing review operations
    @Autowired
    private ReviewService reviewService;

    // Instance variable for managing user operations
    @Autowired
    private UserService userService;
    
    // This method handles GET requests for the "/review" page.It checks the login status of the user and passes the list of reviews to the view for rendering.
    @GetMapping("/review")
    public String review(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        // If the "login" cookie is empty, the user is not logged in
        if(login.isEmpty()){
            model.addAttribute("user_login", false);
        }else{
            model.addAttribute("user_login", true);
            model.addAttribute("userName", login);
        }

        model.addAttribute("reviews", reviewService.getAllReviews());
        return "review/review";
    }

    //This method handles GET requests for the "/review/add" page.It checks if the user is logged in and redirects to the review page if not.
    @GetMapping("/review/add")
    public String addReview(@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/review";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        return "review/addreview";
    }

    //This method handles POST requests for adding a new review.It processes the submitted review content and adds the review using the ReviewManager.
    @PostMapping("/review/add")
    public String createReview(@CookieValue(value = "login", defaultValue = "") String login, @RequestParam String contenReview) {

        reviewService.addReview(login, contenReview);;
        return "redirect:/review";
    }

    @GetMapping("/review/{id}/edit")
    public String editPageReview(@PathVariable Long id,@CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/login";
        }
        Review review = reviewService.getReview(id);
        if(review == null){
            return "redirect:/review";
        }
        User users = userService.getUser(login);
        if(!review.isAutorReview(users)){
            model.addAttribute("error", "Error: The review is not yours");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("Review", review);
        return "review/editReview";
    }
    
    //This method handles POST requests for editing an existing review.It processes the submitted review content and updates the review using the ReviewManager.
    @PostMapping("/review/{id}/edit")
    public String editProcessReview(@PathVariable Long id, @RequestParam String contenReview) {
        reviewService.editReview(id, contenReview);
        return "redirect:/review";
    }

    //This method handles GET requests for the "/review/{id}/delete" page.It checks if the user is logged in and if the review exists before confirming deletion.
    @GetMapping("/review/{id}/delete")
    public String questionRemoveReview(@PathVariable Long id, @CookieValue(value = "login", defaultValue = "") String login, Model model) {
        if(login.isEmpty()){
            return "redirect:/review";
        }
        if(!reviewService.isReviewExist(id)){
            model.addAttribute("error", "Error: The review does not exist");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        Review review = reviewService.getReview(id);
        User users = userService.getUser(login);
        if(!review.isAutorReview(users)){
            model.addAttribute("error", "Error: The review is not yours");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        model.addAttribute("user_login", true);
        model.addAttribute("userName", login);
        model.addAttribute("review", review);
        return "review/are_your_sure_delete_review";
    }

    //This method handles GET requests for deleting a review based on the user's confirmation.It checks if the review exists and deletes it if confirmed.
    @GetMapping("/review/{id}/delete/{action}")
    public String deleteReview(@PathVariable Long id, @PathVariable String action, Model model) {
        if(!reviewService.isReviewExist(id)){
            model.addAttribute("error", "Error: The review does not exist");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        if(action.equals("true")){
            Review review = reviewService.getReview(id);
            reviewService.deleteReview(review);
        }
        return "redirect:/review";
    }    
}

