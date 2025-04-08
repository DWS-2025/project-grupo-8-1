package es.dws.gym.gym.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.dto.CreateGymClassDTO;
import es.dws.gym.gym.dto.GymClassDTO;
import es.dws.gym.gym.model.Gimclass;
import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.GimclassRepository;

/**
 * GymClassService.java
 *
 * This service class handles gym class-related operations such as creating, updating,
 * deleting, and retrieving gym classes. It interacts with the GimclassRepository to perform
 * CRUD operations on the Gimclass entity.
 */


@Service
public class GymClassService {

    @Autowired
    private GimclassRepository gimclassRepository;

    @Autowired
    private UserService userService;

    // This method retrieves all gym classes from the database and converts them to GymClassDTO objects.
    public List<GymClassDTO> getAllGymClassesAsDTO() {
        return gimclassRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // This method retrieves a gym class by ID and converts it to a GymClassDTO object.
    public GymClassDTO getGymClassAsDTO(Long id) {
        Gimclass gymClass = getGimClass(id);
        return gymClass == null ? null : convertToDTO(gymClass);
    }

    // This method creates a new gym class and saves it to the database.
    public GymClassDTO createGymClass(CreateGymClassDTO createGymClassDTO) {
        Gimclass gymClass = new Gimclass(
                createGymClassDTO.name(),
                createGymClassDTO.descript(),
                createGymClassDTO.time(),
                createGymClassDTO.duration()
        );

        // Add users to the class if provided
        if (createGymClassDTO.users() != null) {
            List<User> users = createGymClassDTO.users().stream()
                    .map(userService::getUser)
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
            gymClass.getUsers().addAll(users);
        }

        gimclassRepository.save(gymClass); // Save to generate the ID
        return convertToDTO(gymClass);
    }

    // This method updates an existing gym class based on the provided GymClassDTO object.
    public GymClassDTO updateGymClass(Long id, GymClassDTO gymClassDTO) {
        Gimclass gymClass = gimclassRepository.findById(id).orElse(null);
        if (gymClass == null) {
            return null;
        }
        gymClass.setName(gymClassDTO.name());
        gymClass.setDescript(gymClassDTO.descript());
        gymClass.setTime(gymClassDTO.time());
        gymClass.setDuration(gymClassDTO.duration());
        gimclassRepository.save(gymClass);
        return convertToDTO(gymClass);
    }

    // This method deletes a gym class by ID and returns the deleted gym class as a GymClassDTO object.
    public GymClassDTO deleteGymClassAndReturnDTO(Long id) {
        Gimclass gymClass = gimclassRepository.findById(id).orElse(null);
        if (gymClass == null) {
            return null;
        }
        GymClassDTO gymClassDTO = convertToDTO(gymClass);
        gimclassRepository.delete(gymClass);
        return gymClassDTO;
    }

    // This method converts a Gimclass object to a GymClassDTO object.
    private GymClassDTO convertToDTO(Gimclass gymClass) {
        List<String> userIds = gymClass.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return new GymClassDTO(
                gymClass.getId(),
                gymClass.getName(),
                gymClass.getDescrip(),
                gymClass.getTime(),
                gymClass.getDuration(),
                userIds
        );
    }

    // This method checks if a gym class with the given ID exists in the database.
    public void addClass(String name, String descript, String time, String duration) {
        Gimclass gimclass = new Gimclass(name, descript, formatSQLDate(time), formatSQLTime(duration));
        gimclassRepository.save(gimclass);
    }

    // This method retrieves a gym class by ID.
    public Gimclass getGimClass(Long id) {
        return gimclassRepository.findById(id).orElse(null);
    }

    // This method retrieves all gym classes from the database.
    public List<Gimclass> getListGymClass() {
        return gimclassRepository.findAll();
    }

    // This method checks if a gym class with the given ID exists in the database.
    public void updateClass(Long id, String name, String descript, String time, String duration) {
        Gimclass gimclass = gimclassRepository.findById(id).orElse(null);
        if (gimclass != null) {
            gimclass.setName(name);
            gimclass.setDescript(descript);
            gimclass.setTime(formatSQLDate(time));
            gimclass.setDuration(formatSQLTime(duration));
            gimclassRepository.save(gimclass);
        }
    }

    // This method deletes a gym class by ID.
    public void deleteClass(Long id) {
        Gimclass gimclass = gimclassRepository.findById(id).orElse(null);
        gimclassRepository.delete(gimclass);
    }

    // This method toggles a user's enrollment in a gym class.
    public void toggleUserInClass(Long classId, String userId) {
        Gimclass gimclass = gimclassRepository.findById(classId).orElse(null);
        User user = userService.getUser(userId);
        if (gimclass != null && user != null) {
            if (gimclass.getUsers().contains(user)) {
                gimclass.getUsers().remove(user);
            } else {
                gimclass.getUsers().add(user);
            }
            gimclassRepository.save(gimclass);
        }
    }

    // This method checks if a user is already enrolled in a gym class.
    private Date formatSQLDate(String date) {
        try {
            return Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

    // This method checks if a user is already enrolled in a gym class.
    private Time formatSQLTime(String time) {
        try {
            return Time.valueOf(time);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time format. Expected format: HH:mm:ss");
        }
    }
}
