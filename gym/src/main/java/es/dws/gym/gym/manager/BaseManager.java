package es.dws.gym.gym.manager;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//BaseManager provides utility methods for reading and writing JSON files.
//It uses Jackson's ObjectMapper to handle serialization and deserialization.

public class BaseManager {

    // Reads data from a JSON file and converts it into the specified type.
    protected <T> T readFromDisk(String filePath, TypeReference<T> typeRef) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON", e);
        }
    }

    // Writes an object to a JSON file.
    protected <T> void writeToDisk(String filePath, T data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON", e);
        }
    }
}
