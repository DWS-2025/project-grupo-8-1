package es.dws.gym.gym.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        User user = userService.getUser(reviewDto.user());
        if (user == null) {
            return null;
        }
        Date sqlDate = Date.valueOf(reviewDto.date());
        Review review = new Review(reviewDto.content(), sqlDate);
        review.setUser(user);
        reviewRepository.save(review); // Guardar la reseña para generar el ID
        return convertToDTO(review);
    }

    // This method updates an existing review based on the provided ReviewDTO object.
    public ReviewDTO updateReview(Long id, ReviewDTO updateDto) {
        Review review = getReview(id);
        if (review == null) {
            return null;
        }
        review.setContent(updateDto.content());
        reviewRepository.save(review);
        return convertToDTO(review);
    }

    // This method deletes a review by ID and returns the deleted review as a ReviewDTO object.
    public ReviewDTO deleteReviewAndReturnDTO(Long id) {
        Review review = getReview(id);
        if (review == null) {
            return null;
        }
        ReviewDTO reviewDTO = convertToDTO(review);
        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);
        return reviewDTO;
    }

    // This method converts a Review object to a ReviewDTO object.
    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getId(),
                review.getContent(),
                review.getLocalDateTime().toString()
        );
    }

    // This method adds a review to a user and saves it to the database.
    public void addReview(String userName, String content) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        User user = userService.getUser(userName);
        Review review = new Review(content, sqlDate);
        review.setUser(user);
        user.getReviews().add(review);
        userRepository.save(user);
    }

    // This method retrieves a review by ID.
    public Review getReview(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    // This method checks if a review exists by ID.
    public boolean isReviewExist(Long id) {
        return getReview(id) != null;
    }

    // This method retrieves all reviews from the database.
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // This method retrieves all reviews for a specific user.
    public void editReview(Long id, String content) {
        Review review = getReview(id);
        review.setContent(content);
        reviewRepository.save(review);
    }

    // This method deletes a review by ID.
    public void deleteReview(Review review) {
        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);
    }
}
