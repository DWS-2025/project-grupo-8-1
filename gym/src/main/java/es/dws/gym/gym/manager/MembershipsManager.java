package es.dws.gym.gym.manager;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.Memberships;

@Service
public class MembershipsManager extends BaseManager {
    private Map <String,Memberships> embershipsMap;

    public MembershipsManager(){
        this.embershipsMap = readFromDisk(FilesWeb.MEMBRERSHIPSMAPFILE, new TypeReference<Map<String,Memberships>>() {});
    }
}
