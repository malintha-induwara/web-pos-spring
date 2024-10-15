package lk.ijse.gdse68.webposspring.util;


import lk.ijse.gdse68.webposspring.exception.InvalidImageTypeException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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


    /**
     * @param itemId Item ID
     * @return Image as a Base64 encoded string if the image is found, otherwise null
     */
    public String getImage(String itemId) {
        try {
            Optional<Path> resource = searchImage(itemId);
            if (resource.isPresent()) {
                return Base64.getEncoder().encodeToString(Files.readAllBytes(resource.get()));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImage(String itemId) {
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


    private Optional<Path> searchImage(String itemId) {
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

    public String updateImage(String itemId, MultipartFile file) {
        try {
            Optional<Path> resource = searchImage(itemId);
            if (resource.isPresent()) {
                Files.delete(resource.get());
            }
            return saveImage(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String saveImage(MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return null;
        }

        //Check whether the file types are valid
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpg") &&
                !Objects.requireNonNull(file.getOriginalFilename()).endsWith("png") &&
                !Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpeg")
        ) {
            throw new InvalidImageTypeException("Invalid file type. Only JPG and PNG files are allowed.");
        }
        //Random UUID
        String fileName = UUID.randomUUID().toString();
        try {
            Files.copy(file.getInputStream(), IMAGE_DIRECTORY.resolve(fileName + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1]));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

