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

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public List<ReviewDTO> getAllReviewsAsDTO() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewAsDTO(Long id) {
        Review review = getReview(id);
        return review == null ? null : convertToDTO(review);
    }

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

    public ReviewDTO updateReview(Long id, ReviewDTO updateDto) {
        Review review = getReview(id);
        if (review == null) {
            return null;
        }
        review.setContent(updateDto.content());
        reviewRepository.save(review);
        return convertToDTO(review);
    }

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

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getId(),
                review.getContent(),
                review.getLocalDateTime().toString()
        );
    }

    public void addReview(String userName, String content) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        User user = userService.getUser(userName);
        Review review = new Review(content, sqlDate);
        review.setUser(user);
        user.getReviews().add(review);
        userRepository.save(user);
    }

    public Review getReview(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public boolean isReviewExist(Long id) {
        return getReview(id) != null;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void editReview(Long id, String content) {
        Review review = getReview(id);
        review.setContent(content);
        reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);
    }
}
