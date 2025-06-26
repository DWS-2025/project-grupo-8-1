package es.dws.gym.gym.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArchiveService{

    @Value("${archive.upload.dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE_BYTES = 20 * 1024 * 1024; // 20MB
    private static final String ALLOWED_EXTENSION = "pdf";

    /**
     * Saves a PDF file to disk, preserving the original filename.
     * @param fileBytes file content
     * @param originalFileName original filename
     * @return saved filename
     * @throws IOException if an I/O error occurs
     */
    public String saveArchive(byte[] fileBytes, String originalFileName) throws IOException {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This file is empty");
        }
        if (fileBytes.length > MAX_FILE_SIZE_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This file is too large, maximum size is 20MB");
        }
        String fileExtension = getFileExtension(originalFileName);
        if (!ALLOWED_EXTENSION.equals(fileExtension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF files are allowed");
        }
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String filename = StringUtils.cleanPath(sanitizedFileName);

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid file path");
        }

        Files.write(filePath, fileBytes);
        return filename;
    }

    /**
     * Retrieves the PDF file by its name.
     * @param fileName name of the file
     * @return file content
     * @throws IOException if an I/O error occurs
     */
    public byte[] getArchive(String fileName) throws IOException {
        String sanitizedFileName = sanitizeFileName(fileName);
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(sanitizedFileName).normalize();

        if (!filePath.startsWith(uploadPath) || !Files.exists(filePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        return Files.readAllBytes(filePath);
    }

    /**
     * Deletes a PDF file by its name.
     * @param fileName name of the file to delete
     * @throws IOException if an I/O error occurs
     * @throws ResponseStatusException if the file path is invalid or the file does not exist
     */
    public void deleteArchive(String fileName) throws IOException {
        String sanitizedFileName = sanitizeFileName(fileName);
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(sanitizedFileName).normalize();

        if (!filePath.startsWith(uploadPath)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid file path");
        }
        Files.deleteIfExists(filePath);
    }

    // Returns the file extension of the given filename.
    private String getFileExtension(String fileName) {
        int dot = fileName.lastIndexOf(".");
        if (dot == -1) {
            return "";
        }
        return fileName.substring(dot + 1).toLowerCase();
    }

    // Sanitizes the filename by replacing invalid characters with underscores.
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.\\- ]", "_");
    }
}