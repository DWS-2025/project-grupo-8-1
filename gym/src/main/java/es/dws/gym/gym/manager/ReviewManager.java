package es.dws.gym.gym.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Review;

/**
 * ReviewManager is responsible for managing user reviews.
 * It allows adding, removing, and listing reviews while storing them in a persistent file.
 */
@Service
public class ReviewManager extends BaseManager {

    // Stores reviews mapped by their unique ID
    private Map<Integer, Review> reviewMap;

    // Counter to track the latest review ID
    private Integer contId;

    /**
     * Constructor for ReviewManager.
     * Loads existing reviews and the last used review ID from storage.
     */
    public ReviewManager() {
        this.reviewMap = readFromDisk(FilesWeb.REVIEWMAPFILE, new TypeReference<Map<Integer, Review>>() {});
        this.contId = readFromDisk(FilesWeb.REVIEWIDCONTFILE, new TypeReference<Integer>() {});
    }

    /**
     * Adds a new review with the given username and content.
     * The review is assigned a unique ID and stored persistently.
     *
     * @param userName The name of the user submitting the review.
     * @param content  The content of the review.
     */
    public void addReview(String userName, String content) {
        Date date = new Date(); // Capture the current date
        Review newReview = new Review(userName, content, date);

        // Store the new review with a unique ID
        this.reviewMap.put(contId, newReview);
        contId++; // Increment the ID counter

        // Persist the updated review data
        writeToDisk(FilesWeb.REVIEWMAPFILE, this.reviewMap);
        writeToDisk(FilesWeb.REVIEWIDCONTFILE, this.contId);
    }

    /**
     * Removes a review from the system using its ID.
     * If the review exists, it is deleted and the updated data is saved.
     *
     * @param reviewId The ID of the review to remove.
     * @return True if the review was found and removed, false otherwise.
     */
    public boolean removeReview(Integer reviewId) {
        if (reviewMap.containsKey(reviewId)) {
            reviewMap.remove(reviewId);
            writeToDisk(FilesWeb.REVIEWMAPFILE, this.reviewMap);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of all stored reviews.
     *
     * @return A list containing all reviews.
     */
    public List<Review> listReview() {
        List<Review> listReview = new ArrayList<>();
        for (Map.Entry<Integer, Review> entry : reviewMap.entrySet()) {
            listReview.add(entry.getValue());
        }
        return listReview;
    }
}

