package es.dws.gym.gym.service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.Gimclass;
import es.dws.gym.gym.repository.GimclassRepository;
import es.dws.gym.gym.model.User;

@Service
public class GymClassService {
    
    @Autowired
    private GimclassRepository gimclassRepository;

    @Autowired
    private UserService userService;

    public void addClass(String name, String descript, String time, String duration){
        Gimclass gimclass = new Gimclass( name, descript, formatSQLDate(time), formatSLQTime(duration));
        gimclassRepository.save(gimclass);
    }

    public Gimclass getGimClass(Long id){
        return gimclassRepository.findById(id).orElse(null);
    }

    public List<Gimclass> getListGymClass(){
        return gimclassRepository.findAll();
    }

    public void updateClass(Long id, String name, String descript, String time, String duration) {
        Gimclass gimclass = gimclassRepository.findById(id).orElse(null);
        if (gimclass != null) {
            gimclass.setName(name);
            gimclass.setDescript(descript);
            gimclass.setTime(formatSQLDate(time));
            gimclass.setDuration(formatSLQTime(duration));
            gimclassRepository.save(gimclass);
        }
    }

    public void deleteClass(Long id){
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

    private Date formatSQLDate(String date){
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return Date.valueOf(dateTime.toLocalDate());
    }

    private Time formatSLQTime(String time){
        DateTimeFormatter timeformater = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localtime = LocalTime.parse(time,timeformater);
        return Time.valueOf(localtime);
    }
}
