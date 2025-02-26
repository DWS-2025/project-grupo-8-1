package es.dws.gym.gym.manager;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Review;

@Service
public class ReviewManager extends BaseManager {
    private Map<Integer,Review> reviewMap;

    public ReviewManager(){
        this.reviewMap = readFromDisk(FilesWeb.REVIEWMAPFILE, new TypeReference<Map<Integer, Review>>() {});
    }

    
}
