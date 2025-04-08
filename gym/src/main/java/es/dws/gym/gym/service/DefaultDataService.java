package es.dws.gym.gym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.Review;
import es.dws.gym.gym.model.User;
//import es.dws.gym.gym.repository.ReviewRepository;
import es.dws.gym.gym.repository.UserRepository;
import jakarta.annotation.PostConstruct;

/**
 * DefaultDataService.java
 *
 * This service class is responsible for initializing default data in the database.
 * It creates a test user and associated reviews when the application starts.
 */

@Service
public class DefaultDataService {
    
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        User prueba = new User("prueba", "prueba", "prueba", "+34898764322", "prueba@prueba.com", "Calle prueba", "prueba", null);
        
        try {
            java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse("21/07/2023");
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            Review review = new Review("Esto es el 1 comentario de prueba", sqlDate);
            Review review2 = new Review("Esto es el 2 comentario de prueba", sqlDate);
            Review review3 = new Review("Esto es el 3 comentario de prueba", sqlDate);
            review.setUser(prueba);
            prueba.getReviews().add(review);
            review2.setUser(prueba);
            prueba.getReviews().add(review2);
            review3.setUser(prueba);
            prueba.getReviews().add(review3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userRepository.save(prueba);
    }
}
