package es.dws.gym.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dws.gym.gym.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
