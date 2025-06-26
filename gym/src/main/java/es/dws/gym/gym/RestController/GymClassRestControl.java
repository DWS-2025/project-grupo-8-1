package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateGymClassDTO;
import es.dws.gym.gym.dto.GymClassDTO;
import es.dws.gym.gym.dto.GymClassToggleDTO;
import es.dws.gym.gym.service.GymClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @PutMapping("/{id}")
    public ResponseEntity<GymClassDTO> updateGymClass(@PathVariable Long id, @RequestBody GymClassDTO gymClassDTO) {
        GymClassDTO updatedClass = gymClassService.updateGymClass(id, gymClassDTO);
        return updatedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedClass);
    }

    // This endpoint deletes a gym class by its ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<GymClassDTO> deleteGymClass(@PathVariable Long id) {
        GymClassDTO deletedClass = gymClassService.deleteGymClassAndReturnDTO(id);
        return deletedClass == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedClass);
    }

    // Endpoint para subir un archivo PDF a una clase
    @PostMapping("/pdf/{id}")
    public ResponseEntity<GymClassDTO> uploadClassPdf(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            GymClassDTO updatedClass = gymClassService.updateClassPdf(
                id,
                file.getBytes(),
                file.getOriginalFilename()
            );
            if (updatedClass == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedClass);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Endpoint para descargar el archivo PDF asociado a una clase
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> downloadClassPdf(@PathVariable Long id) {
        try {
            byte[] pdfBytes = gymClassService.getClassPdf(id);
            // Obtener el nombre real del archivo desde la entidad Gimclass
            String fileName = "class_" + id + ".pdf";
            var gimclass = gymClassService.getGimClass(id);
            if (gimclass != null && gimclass.getPdfFile() != null) {
                fileName = gimclass.getPdfFile();
            }
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Añadir usuario a una clase (POST)
    @PostMapping("/toggle/{id}")
    public ResponseEntity<GymClassDTO> addUserToClass(@PathVariable Long id, @RequestBody GymClassToggleDTO dto) {
        try {
            GymClassDTO updatedClass = gymClassService.addUserToClass(id, dto.userId());
            if (updatedClass == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedClass);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Quitar usuario de una clase (DELETE)
    @DeleteMapping("/toggle/{id}")
    public ResponseEntity<GymClassDTO> removeUserFromClass(@PathVariable Long id, @RequestBody GymClassToggleDTO dto) {
        try {
            GymClassDTO updatedClass = gymClassService.removeUserFromClass(id, dto.userId());
            if (updatedClass == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedClass);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
