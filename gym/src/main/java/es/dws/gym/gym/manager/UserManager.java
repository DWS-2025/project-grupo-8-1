package es.dws.gym.gym.manager;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import es.dws.gym.gym.FilesWeb;
import es.dws.gym.gym.model.User;

@Service
public class UserManager extends BaseManager {
    private Map <String,User> users = new HashMap<String,User>();

    public UserManager(){
        this.users = readFromDisk(FilesWeb.USERSMAPFILE, new TypeReference<Map<String, User>>() {});
    }

    public boolean addUser(String userName, String firstName, String sureName, String telephone, String mail, String address, String password){
        if(!users.containsKey(userName)){
            User user = new User(firstName, sureName,telephone,mail,address,password);
            this.users.put(userName,user);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }else{
            return false;
        }    
    }

    public boolean removeUser(String userName){
        if(users.containsKey(userName)){
            this.users.remove(userName);
            writeToDisk(FilesWeb.USERSMAPFILE, this.users);
            return true;
        }
        return false;
    }

    public boolean login(String name, String password){
        if(!users.containsKey(name)){
            return false;
        }
        User user = users.get(name);
        if(!user.getPassword().equals(password)){
            return false;
        }
        return true;
    }
    
    public User getUser(String name){
        return this.users.get(name);
    }

    public boolean isUser(String name){
        return users.containsKey(name);
    }

    public List<String> getUserList(String name){
        User user = getUser(name);
        return user.ListUser();
    }

    public void setPassword(String name, String password){
        users.get(name).setPassword(password);
        writeToDisk(FilesWeb.USERSMAPFILE, this.users);
    }
}
