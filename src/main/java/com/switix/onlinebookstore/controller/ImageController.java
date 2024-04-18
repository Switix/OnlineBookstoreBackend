package com.switix.onlinebookstore.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/images")
public class ImageController {

    @GetMapping("{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        Resource image = new FileSystemResource("src/main/resources/static/images/" + imageName);

        if (image.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                String uploadDir = "src/main/resources/static/images/";


                // Generate a timestamp using System.currentTimeMillis()
                String timestamp = Long.toString(System.currentTimeMillis());

                // Get the original filename and file extension
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

                // Create the new filename with timestamp
                String filenameWithTimestamp = originalFilename.replace(fileExtension, "_" + timestamp + fileExtension);

                Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                // Define the path to save the uploaded file with timestamp in filename
                Path path = dirPath.resolve(filenameWithTimestamp);

                try (OutputStream outputStream = Files.newOutputStream(path)) {
                    byte[] buffer = new byte[4096]; // Use a larger buffer size
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    return ResponseEntity.ok(filenameWithTimestamp);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading file");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }
    }
}
