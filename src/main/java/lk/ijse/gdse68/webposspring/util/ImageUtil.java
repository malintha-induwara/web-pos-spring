package lk.ijse.gdse68.webposspring.util;


import lk.ijse.gdse68.webposspring.exception.InvalidImageTypeException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Component
public class ImageUtil {
    public static Path IMAGE_DIRECTORY = Paths.get(System.getProperty("user.home"), "Desktop", "LocalS3Bucket", "upload").toAbsolutePath().normalize();

    public ImageUtil() {
        if (!Files.exists(IMAGE_DIRECTORY)) {
            try {
                Files.createDirectories(IMAGE_DIRECTORY);
                System.out.println("Directory Created");
            } catch (IOException e) {
                System.out.println("Failed to Create directory " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static String getImage(String itemId) {
        try {
            Optional<Path> resource = searchImage(itemId);
            if (resource.isPresent()) {
               return Base64.getEncoder().encodeToString(Files.readAllBytes(resource.get()));
            }else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteImage(String itemId) {
        try {
            Optional<Path> resource = searchImage(itemId);
            if (resource.isPresent()) {
                Files.delete(resource.get());
            }
        } catch (IOException e) {
            System.err.println("Error Deleting file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private static Optional<Path> searchImage(String itemId) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(IMAGE_DIRECTORY, itemId + ".*")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    return Optional.of(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error searching for file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static void saveImage(String itemId, MultipartFile file) {
        try {
            // Check if the file is empty
            if (file==null || file.isEmpty()) {
                return;
            }

            if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpg") &&
                    !Objects.requireNonNull(file.getOriginalFilename()).endsWith("png") &&
                    !Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpeg")
            ) {
                throw new InvalidImageTypeException("Invalid file type. Only JPG and PNG files are allowed.");
            }

            Optional<Path> resource = searchImage(itemId);
            if (resource.isPresent()) {
                Files.delete(resource.get());
            }

            Files.copy(file.getInputStream(), IMAGE_DIRECTORY.resolve(itemId + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

