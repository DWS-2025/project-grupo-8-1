package es.dws.gym.gym.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import es.dws.gym.gym.dto.CreateReviewDTO;
import es.dws.gym.gym.dto.ReviewDTO;
import es.dws.gym.gym.model.Review;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.ReviewRepository;
import es.dws.gym.gym.repository.UserRepository;

/* 
 * ReviewService.java
 * 
 * This service class handles review-related operations such as creating, updating,
 * deleting, and retrieving reviews. It interacts with the ReviewRepository to perform
 * CRUD operations on the Review entity.
 */


@Service
public class ReviewService implements es.dws.gym.gym.mapper.reviewMapper {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Private method to sanitize the HTML content of the review
    private String sanitizeReviewContent(String content) {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
        return policy.sanitize(content);
    }

    // This method converts a Review to ReviewDTO and SANITIZES the content
    @Override
    public ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
            review.getId(),
            review.getUser().getId().toString(),
            sanitizeReviewContent(review.getContent()),
            review.getLocalDateTime().toString()
        );
    }

    // This method converts a Review entity to a ReviewDTO object.
    private ReviewDTO convertToDTO(Review review) {
        return toDTO(review);
    }

    // This method retrieves all reviews from the database and converts them to ReviewDTO objects.
    public List<ReviewDTO> getAllReviewsAsDTO() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // This method retrieves a review by ID and converts it to a ReviewDTO object.
    public ReviewDTO getReviewAsDTO(Long id) {
        Review review = getReview(id);
        return review == null ? null : convertToDTO(review);
    }

    // This method creates a new review and saves it to the database.
    public ReviewDTO createReview(CreateReviewDTO reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.getUser(userId);
        if (user == null) {
            return null;
        }
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String safeContent = sanitizeReviewContent(reviewDto.content());
        Review review = new Review(safeContent, sqlDate);
        review.setUser(user);
        reviewRepository.save(review); // Save the review to generate the ID
        return convertToDTO(review);
    }

    // This method updates an existing review based on the provided ReviewDTO object.
    public ReviewDTO updateReview(Long id, ReviewDTO updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = getReview(id);
        if (review == null || !isUserAccessReview(authentication, review)) {
            return null;
        }
        String safeContent = sanitizeReviewContent(updateDto.content());
        review.setContent(safeContent);
        reviewRepository.save(review);
        return convertToDTO(review);
    }

    // This method deletes a review by ID and returns the deleted review as a ReviewDTO object.
    public ReviewDTO deleteReviewAndReturnDTO(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = getReview(id);
        if (review == null || !isUserAccessReview(authentication, review)) {
            return null;
        }
        ReviewDTO reviewDTO = convertToDTO(review);
        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);
        return reviewDTO;
    }

    // This method adds a review to a user and saves it to the database.
    public void addReview(String content) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        User user = userService.getUser(userId);
        String safeContent = sanitizeReviewContent(content);
        Review review = new Review(safeContent, sqlDate);
        review.setUser(user);
        user.getReviews().add(review);
        userRepository.save(user);
    }

    // This method retrieves a review by ID.
    public Review getReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setContent(sanitizeReviewContent(review.getContent()));
        }
        return review;
    }

    // This method checks if a review exists by ID.
    public boolean isReviewExist(Long id) {
        return getReview(id) != null;
    }

    // This method retrieves all reviews from the database and SANITIZES the content of each one
    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        for (Review review : reviews) {
            review.setContent(sanitizeReviewContent(review.getContent()));
        }
        return reviews;
    }

    // This method retrieves all reviews for a specific user.
    public boolean editReview(Long id, String content) {
        Review review = getReview(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(!isUserAccessReview(authentication, review)){
            return false;
        } 

        String safeContent = sanitizeReviewContent(content);
        review.setContent(safeContent);
        reviewRepository.save(review);
        return true;
    }

    // This method deletes a review by ID.
    public boolean deleteReview(Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(!isUserAccessReview(authentication, review)){
            return false;
        } 

        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);
        return true;
    }

    // This method retrieves paginated reviews and converts them to ReviewDTO objects.
    public Page<ReviewDTO> getReviewsPaginated(int page, int size) {
        return reviewRepository.findAll(PageRequest.of(page, size))
                .map(this::convertToDTO);
    }

    // This method checks if the user has access to the review based on their role and ownership of the review.
    public boolean isUserAccessReview(Authentication authentication, Review review) {
        // If the user is an administrator, grant access
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        // If the user is a regular user, check if they are the author of the review
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
