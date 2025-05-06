package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateReviewDTO;
import es.dws.gym.gym.dto.ReviewDTO;
import es.dws.gym.gym.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.net.URI;
import java.util.List;
import java.util.Map;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

/**
 * ReviewRestControl.java
 *
 * This class is a REST controller that handles HTTP requests related to reviews.
 * It provides endpoints for listing, retrieving, creating, updating, and deleting reviews.
 * The controller uses the ReviewService to perform the actual operations on the data.
 */

@RestController
@RequestMapping("/api/review")
public class ReviewRestControl {

    @Autowired
    private ReviewService reviewService;

    // This endpoint retrieves all reviews and returns them as a list of ReviewDTO objects.
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> listReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviewsAsDTO();
        return ResponseEntity.ok(reviews);
    }

    // This endpoint retrieves reviews in a paginated format.
    @GetMapping("/paginated")
    public ResponseEntity<?> listReviewsPaginated(@RequestParam int page, @RequestParam int size) {
        Page<ReviewDTO> reviewsPage = reviewService.getReviewsPaginated(page - 1, size);
        return ResponseEntity.ok(Map.of(
            "reviews", reviewsPage.getContent(),
            "hasMore", reviewsPage.hasNext()
        ));
    }

    // This endpoint retrieves a specific review by its ID.
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewAsDTO(id);
        return review == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(review);
    }

    // This endpoint creates a new review and returns it as a DTO.
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody CreateReviewDTO reviewDto) {
        ReviewDTO createdReview = reviewService.createReview(reviewDto);
        if (createdReview == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdReview.id()).toUri();
        return ResponseEntity.created(location).body(createdReview);
    }

    // This endpoint updates an existing review and returns it as a DTO.
    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO updateDto) {
        ReviewDTO updatedReview = reviewService.updateReview(updateDto.id(), updateDto);
        return updatedReview == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedReview);
    }

    // This endpoint deletes a review by its ID and returns the deleted review as a DTO.
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id) {
        ReviewDTO deletedReview = reviewService.deleteReviewAndReturnDTO(id);
        return deletedReview == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedReview);
    }
}
