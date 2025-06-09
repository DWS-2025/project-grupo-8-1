package es.dws.gym.gym.controller;
import es.dws.gym.gym.model.Review;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.service.ReviewService;
import es.dws.gym.gym.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// This class handles the review-related views and operations.
@Controller
public class ReviewWebControl {

    // Instance variable for managing review operations
    @Autowired
    private ReviewService reviewService;

    // Instance variable for managing user operations
    @Autowired
    private UserService userService;
    
    // This method handles GET requests for the "/review" page.
    @GetMapping("/review")
    public String review(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "review/review";
    }

    // This method handles GET requests for the "/review/add" page.
    @GetMapping("/review/add")
    public String addReview(Model model) {
        return "review/addreview";
    }

    // This method handles POST requests for adding a new review. It processes the submitted review content and adds the review using the ReviewManager.
    @PostMapping("/review/add")
    public String createReview(@RequestParam String contenReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        reviewService.addReview(userId, contenReview);
        return "redirect:/review";
    }

    // This method handles GET requests for the "/review/{id}/edit" page. It checks if the user is logged in and if the review exists before allowing editing.
    @GetMapping("/review/{id}/edit")
    public String editPageReview(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewService.getReview(id);

        if (review == null) {
            return "redirect:/review";
        }

        if (!isUserAccessReview(authentication, review)) {
            model.addAttribute("error", "Error: Not authorized to edit this review");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }

        model.addAttribute("Review", review);
        return "review/editReview";
    }
    
    // This method handles POST requests for editing an existing review. It processes the submitted review content and updates the review using the ReviewManager.
    @PostMapping("/review/{id}/edit")
    public String editProcessReview(@PathVariable Long id, @RequestParam String contenReview, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewService.getReview(id);

        if (review == null) {
            return "redirect:/review";
        }

        if (!isUserAccessReview(authentication, review)) {
            model.addAttribute("error", "Error: Not authorized to edit this review");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        
        reviewService.editReview(id, contenReview);
        return "redirect:/review";
    }

    // This method handles GET requests for the "/review/{id}/delete" page. It checks if the user is logged in and if the review exists before confirming deletion.
    @GetMapping("/review/{id}/delete")
    public String questionRemoveReview(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewService.getReview(id);

        if (review == null) {
            return "redirect:/review";
        }

        if (!isUserAccessReview(authentication, review)) {
            model.addAttribute("error", "Error: Not authorized to edit this review");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }

        model.addAttribute("review", review);
        return "review/are_your_sure_delete_review";
    }

    // This method handles POST requests for deleting a review. It checks if the review exists and if the user is authorized to delete it.
    @PostMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable Long id, @RequestParam String action, Model model) {
        if (!reviewService.isReviewExist(id)) {
            model.addAttribute("error", "Error: The review does not exist");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }

        Review review = reviewService.getReview(id);

        if ("true".equals(action)) {
            boolean deleteReview = reviewService.deleteReview(review);
            if (!deleteReview) {
            model.addAttribute("error", "Error: Not authorized to edit this review");
            model.addAttribute("error_redirect", "/review");
            return "error";
        }
        }
        return "redirect:/review";
    }    

    // Función privada para comprobar permisos sobre una review
    private boolean isUserAccessReview(Authentication authentication, Review review) {
        // Si es ADMIN, siempre true
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        // Si es USER y es autor de la review
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            String userId = authentication.getPrincipal() instanceof UserDetails ? ((UserDetails) authentication.getPrincipal()).getUsername() : null;
            if (userId != null && review != null) {
                User user = userService.getUser(userId);
                return review.isAutorReview(user);
            }
        }
        return false;
    }
}

