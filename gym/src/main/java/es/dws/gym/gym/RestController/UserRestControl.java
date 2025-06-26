package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateUserDTO;
import es.dws.gym.gym.dto.UpdateUserDTO;
import es.dws.gym.gym.dto.ViewUserDTO;
import es.dws.gym.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserRestControl {
    
    //this class is used to handle user-related REST API requests
    @Autowired
    private UserService userService;

    // The @RestController annotation indicates that this class is a REST controller
    @GetMapping
    public ResponseEntity<List<ViewUserDTO>> listUsers() {
        List<ViewUserDTO> users = userService.getAllViewUsersDTO();
        return ResponseEntity.ok(users);
    }

    // The @GetMapping annotation maps HTTP GET requests onto specific handler methods
    @GetMapping("/{id}")
    public ResponseEntity<ViewUserDTO> getUser(@PathVariable String id) {
        ViewUserDTO viewUserDTO = userService.getViewUserAsDTO(id);
        return viewUserDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(viewUserDTO);
    }

    // The @PostMapping annotation maps HTTP POST requests onto specific handler methods
    @PostMapping
    public ResponseEntity<CreateUserDTO> createUser(@RequestBody CreateUserDTO newUser) {
        CreateUserDTO createdUser = userService.createUser(newUser);
        if (createdUser == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.id()).toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String id) {
        try {
            byte[] image = userService.getUserImage(id);
            if (image == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(image);
        } catch (UserService.UserServiceException e) {
            return ResponseEntity.status(403).build();
        } catch (IOException e) {
            return ResponseEntity.status(503).build();
        }
    } 

    @PutMapping("/image/{id}")
    public ResponseEntity<byte[]> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            userService.saveUserImage(id, file);
            byte[] image = userService.getUserImage(id);
            if (image == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(image);
        } catch (UserService.UserServiceException e) {
            return ResponseEntity.status(403).build();
        } catch (IOException e) {
            return ResponseEntity.status(503).build();
        }
    }

    // The @PutMapping annotation maps HTTP PUT requests onto specific handler methods
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO updatedUser) {
        UpdateUserDTO userDTO = userService.updateUser(id, updatedUser);
        return userDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userDTO);
    }

    // The @DeleteMapping annotation maps HTTP DELETE requests onto specific handler methods
    @DeleteMapping("/{id}")
    public ResponseEntity<ViewUserDTO> deleteUser(@PathVariable String id) {
        ViewUserDTO deletedUser = userService.deleteUserAndReturnDTO(id);
        return deletedUser == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedUser);
    }
}