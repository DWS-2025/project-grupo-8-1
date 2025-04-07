package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateReviewDTO;
import es.dws.gym.gym.dto.ReviewDTO;
import es.dws.gym.gym.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/review")
public class ReviewRestControl {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> listReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviewsAsDTO();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewAsDTO(id);
        return review == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody CreateReviewDTO reviewDto) {
        ReviewDTO createdReview = reviewService.createReview(reviewDto);
        if (createdReview == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdReview.id()).toUri();
        return ResponseEntity.created(location).body(createdReview);
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO updateDto) {
        ReviewDTO updatedReview = reviewService.updateReview(updateDto.id(), updateDto);
        return updatedReview == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id) {
        ReviewDTO deletedReview = reviewService.deleteReviewAndReturnDTO(id);
        return deletedReview == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedReview);
    }
}
