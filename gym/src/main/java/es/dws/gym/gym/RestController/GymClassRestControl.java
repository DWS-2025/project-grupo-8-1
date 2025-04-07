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

@RestController
@RequestMapping("/api/gymclass")
public class GymClassRestControl {

    @Autowired
    private GymClassService gymClassService;

    @GetMapping
    public ResponseEntity<List<GymClassDTO>> listClasses() {
        List<GymClassDTO> classes = gymClassService.getAllGymClassesAsDTO();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymClassDTO> getGymClass(@PathVariable Long id) {
        GymClassDTO gymClass = gymClassService.getGymClassAsDTO(id);
        return gymClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(gymClass);
    }

    @PostMapping
    public ResponseEntity<GymClassDTO> createGymClass(@RequestBody CreateGymClassDTO createGymClassDTO) {
        GymClassDTO createdClass = gymClassService.createGymClass(createGymClassDTO);
        if (createdClass == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdClass.id()).toUri();
        return ResponseEntity.created(location).body(createdClass);
    }

    @PutMapping
    public ResponseEntity<GymClassDTO> updateGymClass(@RequestBody GymClassDTO gymClassDTO) {
        GymClassDTO updatedClass = gymClassService.updateGymClass(gymClassDTO.id(), gymClassDTO);
        return updatedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedClass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GymClassDTO> deleteGymClass(@PathVariable Long id) {
        GymClassDTO deletedClass = gymClassService.deleteGymClassAndReturnDTO(id);
        return deletedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedClass);
    }
}
