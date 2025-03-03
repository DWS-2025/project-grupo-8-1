package es.dws.gym.gym.manager;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BaseManager provides utility methods for reading and writing JSON files.
 * It uses Jackson's ObjectMapper to handle serialization and deserialization.
 */
public class BaseManager {

    /**
     * Reads data from a JSON file and converts it into the specified type.
     *
     * @param <T>      The type of the object to be returned.
     * @param filePath The path of the JSON file to read.
     * @param typeRef  A TypeReference specifying the expected return type.
     * @return The deserialized object from the JSON file.
     * @throws RuntimeException if an error occurs while reading the file.
     */
    protected <T> T readFromDisk(String filePath, TypeReference<T> typeRef) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON", e);
        }
    }

    /**
     * Writes an object to a JSON file.
     *
     * @param <T>      The type of the object to be written.
     * @param filePath The path of the JSON file to write.
     * @param data     The object to be serialized and written to the file.
     * @throws RuntimeException if an error occurs while writing the file.
     */
    protected <T> void writeToDisk(String filePath, T data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON", e);
        }
    }
}
