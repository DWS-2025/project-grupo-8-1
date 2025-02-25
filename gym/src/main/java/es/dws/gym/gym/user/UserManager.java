package es.dws.gym.gym.user;

import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.dws.gym.gym.FilesWeb;

import java.io.File;
import java.io.IOException;

@Service
public class UserManager {
    private Map <String,User> users = new HashMap<String,User>();

    public UserManager(){
        loadUsersDisk();
    }

    public boolean addUser(String userName, String firstName, String sureName, String telephone, String mail, String address, String password){
        if(!users.containsKey(userName)){
            User user = new User(firstName, sureName,telephone,mail,address,password);
            this.users.put(userName,user);
            writeUsersDisk();
            return true;
        }else{
            return false;
        }    
    }

    public boolean removeUser(String userName){
        if(users.containsKey(userName)){
            this.users.remove(userName);
            writeUsersDisk();
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
        
    private void loadUsersDisk(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            this.users = objectMapper.readValue(new File(FilesWeb.USERSMAPFILE), new TypeReference<Map<String, User>>() {});        
        }catch (IOException e){
            throw new RuntimeException("Error reading JSON", e);
        }
    }

    private void writeUsersDisk(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            objectMapper.writeValue(new File(FilesWeb.USERSMAPFILE), this.users);
        }catch (IOException e){
            throw new RuntimeException("Error writing JSON", e);
        }
    }
}
