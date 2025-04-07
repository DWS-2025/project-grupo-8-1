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

@Service
public class GymClassService {

    @Autowired
    private GimclassRepository gimclassRepository;

    @Autowired
    private UserService userService;

    public List<GymClassDTO> getAllGymClassesAsDTO() {
        return gimclassRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GymClassDTO getGymClassAsDTO(Long id) {
        Gimclass gymClass = getGimClass(id);
        return gymClass == null ? null : convertToDTO(gymClass);
    }

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

    public GymClassDTO deleteGymClassAndReturnDTO(Long id) {
        Gimclass gymClass = gimclassRepository.findById(id).orElse(null);
        if (gymClass == null) {
            return null;
        }
        GymClassDTO gymClassDTO = convertToDTO(gymClass);
        gimclassRepository.delete(gymClass);
        return gymClassDTO;
    }

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

    public void addClass(String name, String descript, String time, String duration) {
        Gimclass gimclass = new Gimclass(name, descript, formatSQLDate(time), formatSQLTime(duration));
        gimclassRepository.save(gimclass);
    }

    public Gimclass getGimClass(Long id) {
        return gimclassRepository.findById(id).orElse(null);
    }

    public List<Gimclass> getListGymClass() {
        return gimclassRepository.findAll();
    }

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

    public void deleteClass(Long id) {
        Gimclass gimclass = gimclassRepository.findById(id).orElse(null);
        gimclassRepository.delete(gimclass);
    }

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

    private Date formatSQLDate(String date) {
        try {
            return Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

    private Time formatSQLTime(String time) {
        try {
            return Time.valueOf(time);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time format. Expected format: HH:mm:ss");
        }
    }
}
