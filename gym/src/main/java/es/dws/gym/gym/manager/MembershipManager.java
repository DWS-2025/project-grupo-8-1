package es.dws.gym.gym.manager;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Memberships;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class MembershipManager extends BaseManager {

    private Map<Integer, Memberships> mebershipsMap;
    private Integer mebershipsID;

    // Constructor to initialize the manager by reading data from the disk
    public MembershipManager(){
        this.mebershipsMap = readFromDisk(FilesWeb.MEMBRERSHIPSMAPFILE, new TypeReference<Map<Integer, Memberships>>() {});
        this.mebershipsID = readFromDisk(FilesWeb.MEMBRERSHIPSIDFILE, new TypeReference<Integer>() {});
    }

    // Returns a list of all memberships stored in the map
    public List<Memberships> listMemberships(){
        List<Memberships> listMemberships = new ArrayList<>();
        for(Map.Entry<Integer, Memberships> entry : mebershipsMap.entrySet()){
            listMemberships.add(entry.getValue());
        }
        return listMemberships;
    }

    // Checks if a membership with a given ID exists in the map
    public boolean isExistKey(Integer key){
        return mebershipsMap.containsKey(key);
    }
}

