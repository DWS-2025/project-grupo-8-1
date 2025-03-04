package es.dws.gym.gym.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Gimclass;

import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class GimClassManager extends BaseManager {
    private Map<Integer,Gimclass> gimclassMap;
    private Integer gimclassID;
    

    public GimClassManager(){
        this.gimclassMap = readFromDisk(FilesWeb.CLASSMAPFILE, new TypeReference<Map<Integer,Gimclass>>(){});
        this.gimclassID = readFromDisk(FilesWeb.CLASSIDFILE, new TypeReference<Integer>() {});
    }

    public List<Gimclass> listGimClass(){
        List<Gimclass> listClass = new ArrayList<>();
        for(Map.Entry<Integer,Gimclass> entry : gimclassMap.entrySet()){
            listClass.add(entry.getValue());
        }
        return listClass;
    }
}
