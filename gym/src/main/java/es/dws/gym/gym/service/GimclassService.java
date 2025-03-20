package es.dws.gym.gym.service;

import java.sql.Time;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.Gimclass;
import es.dws.gym.gym.repository.GimclassRepository;

@Service
public class GimclassService {
    
    @Autowired
    private GimclassRepository gimclassRepository;

    public void addClass(String name, String descript, Date time, Time duration){
        Gimclass gimclass = new Gimclass(name, descript, time, duration);
        gimclassRepository.save(gimclass);
    }
}
