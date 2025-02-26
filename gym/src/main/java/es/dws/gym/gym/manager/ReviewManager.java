package es.dws.gym.gym.manager;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Review;

@Service
public class ReviewManager extends BaseManager {
    private Map<Integer,Review> reviewMap;
    private Integer contId;

    public ReviewManager(){
        this.reviewMap = readFromDisk(FilesWeb.REVIEWMAPFILE, new TypeReference<Map<Integer, Review>>() {});
        this.contId = readFromDisk(FilesWeb.REVIEWIDCONTFILE, new TypeReference<Integer>() {});
    }

    public void addReview(String userName, String content){
        LocalDateTime localDateTime = LocalDateTime.now();
        Review newReview = new Review(userName, content, localDateTime);
        this.reviewMap.put(contId, newReview);
        contId++;
        writeToDisk(FilesWeb.REVIEWMAPFILE, this.reviewMap);
        writeToDisk(FilesWeb.REVIEWIDCONTFILE, this.contId);
    }

    public boolean removeReview(Integer reviewId){
        if(reviewMap.containsKey(reviewId)){
            reviewMap.remove(reviewId);
            writeToDisk(FilesWeb.REVIEWMAPFILE, this.reviewMap);
            return true;
        }
        return false;
    }

    
}
