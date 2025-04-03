package es.dws.gym.gym.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addReview(String userName, String content){
        java.util.Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        User user = userService.getUser(userName);
        Review review = new Review(content, sqlDate);
        review.setUser(user);
        user.getReviews().add(review);
        userRepository.save(user);
    }

    public Review getReview(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public boolean isReviewExist(Long id){
        return getReview(id) != null;
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    public void editReview(Long id, String content){
        Review review = getReview(id);
        review.setContent(content);
        reviewRepository.save(review);
    }

    public void deleteReview(Review reviewdelete){
        User user = reviewdelete.getUser();
        user.getReviews().remove(reviewdelete);
        userRepository.save(user);
    }
}
