package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateGymClassDTO;
import es.dws.gym.gym.dto.GymClassDTO;
import es.dws.gym.gym.service.GymClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

/**
 * GymClassRestControl.java
 *
 * This class is a REST controller that handles HTTP requests related to gym classes.
 * It provides endpoints for listing, retrieving, creating, updating, and deleting gym classes.
 * The controller uses the GymClassService to perform the actual operations on the data.
 */

@RestController
@RequestMapping("/api/gymclass")
public class GymClassRestControl {

    @Autowired
    private GymClassService gymClassService;

    // This endpoint retrieves a list of all gym classes.
    @GetMapping
    public ResponseEntity<List<GymClassDTO>> listClasses() {
        List<GymClassDTO> classes = gymClassService.getAllGymClassesAsDTO();
        return ResponseEntity.ok(classes);
    }

    // This endpoint retrieves a specific gym class by its ID.
    @GetMapping("/{id}")
    public ResponseEntity<GymClassDTO> getGymClass(@PathVariable Long id) {
        GymClassDTO gymClass = gymClassService.getGymClassAsDTO(id);
        return gymClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(gymClass);
    }

    // This endpoint retrieves a specific gym class by its ID and returns it as a DTO.
    @PostMapping
    public ResponseEntity<GymClassDTO> createGymClass(@RequestBody CreateGymClassDTO createGymClassDTO) {
        GymClassDTO createdClass = gymClassService.createGymClass(createGymClassDTO);
        if (createdClass == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdClass.id()).toUri();
        return ResponseEntity.created(location).body(createdClass);
    }

    // This endpoint updates an existing gym class.
    @PutMapping
    public ResponseEntity<GymClassDTO> updateGymClass(@RequestBody GymClassDTO gymClassDTO) {
        GymClassDTO updatedClass = gymClassService.updateGymClass(gymClassDTO.id(), gymClassDTO);
        return updatedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedClass);
    }

    // This endpoint deletes a gym class by its ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<GymClassDTO> deleteGymClass(@PathVariable Long id) {
        GymClassDTO deletedClass = gymClassService.deleteGymClassAndReturnDTO(id);
        return deletedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedClass);
    }
}
