package es.dws.gym.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dws.gym.gym.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
}
