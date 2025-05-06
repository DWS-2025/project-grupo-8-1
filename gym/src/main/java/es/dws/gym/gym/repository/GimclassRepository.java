package es.dws.gym.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import es.dws.gym.gym.model.Gimclass;
import es.dws.gym.gym.model.User;

public interface GimclassRepository extends JpaRepository<Gimclass, Long> {
    List<Gimclass> searchByNameContainingIgnoreCase(String name);
    List<Gimclass> searchByUsersContaining(User user); 
}
