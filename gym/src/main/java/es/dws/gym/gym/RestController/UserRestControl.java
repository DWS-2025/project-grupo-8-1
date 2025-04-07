package es.dws.gym.gym.RestController;

import es.dws.gym.gym.dto.CreateUserDTO;
import es.dws.gym.gym.dto.UpdateUserDTO;
import es.dws.gym.gym.dto.ViewUserDTO;
import es.dws.gym.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/user")
public class UserRestControl {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<ViewUserDTO>> listUsers() {
        List<ViewUserDTO> users = userService.getAllViewUsersDTO();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewUserDTO> getUser(@PathVariable String id) {
        ViewUserDTO viewUserDTO = userService.getViewUserAsDTO(id);
        return viewUserDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(viewUserDTO);
    }

    @PostMapping
    public ResponseEntity<CreateUserDTO> createUser(@RequestBody CreateUserDTO newUser) {
        CreateUserDTO createdUser = userService.createUser(newUser);
        if (createdUser == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.id()).toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO updatedUser) {
        UpdateUserDTO userDTO = userService.updateUser(id, updatedUser);
        return userDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ViewUserDTO> deleteUser(@PathVariable String id) {
        ViewUserDTO deletedUser = userService.deleteUserAndReturnDTO(id);
        return deletedUser == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(deletedUser);
    }
}