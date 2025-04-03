package es.dws.gym.gym.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageService {
    
    // Constants for maximum image dimensions
    private final static Integer MAX_WIDTH = 300;
    private final static Integer MAX_HEIGHT = 300;

    // Method to validate the uploaded image
    public boolean validateImage(MultipartFile imageUpload, String redirect, Model model) throws IOException {
        if (imageUpload.isEmpty()){
            model.addAttribute("error", "Invalid image file.");
            model.addAttribute("error_redirect", redirect);
            return false;
        }

        String contentType = imageUpload.getContentType();
        if (contentType == null || !contentType.equalsIgnoreCase("image/jpeg")) {
            model.addAttribute("error", "Invalid image type. Only JPG files are allowed.");
            model.addAttribute("error_redirect", redirect);
            return false;
        }

        BufferedImage image = ImageIO.read(imageUpload.getInputStream());
        if (image == null) {
            model.addAttribute("error", "Invalid image file.");
            model.addAttribute("error_redirect", redirect);
            return false;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            model.addAttribute("error", "Image dimensions exceed the maximum allowed size of 300x200 pixels.");
            model.addAttribute("error_redirect", redirect);
            return false;
        }
        return true;
    }

    // Overloaded method to validate the uploaded image without redirecting
    public boolean validateImage(MultipartFile imageUpload) throws IOException {
        if (imageUpload.isEmpty()){
            return false;
        }

        String contentType = imageUpload.getContentType();
        if (contentType == null || !contentType.equalsIgnoreCase("image/jpeg")) {
            return false;
        }

        BufferedImage image = ImageIO.read(imageUpload.getInputStream());
        if (image == null) {
            return false;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            return false;
        }
        return true;
    }
}
