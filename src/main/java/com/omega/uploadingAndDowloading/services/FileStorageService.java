package com.omega.uploadingAndDowloading.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {
    @Value("${file.upload.photo-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile sourceFile) {
        final String fileUploadSubPath = "products" + File.separator + "categories";
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(MultipartFile sourceFile, String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;

        File targetFile = new File(finalUploadPath);
        if (!targetFile.exists()) {
            boolean folderCreated = targetFile.mkdirs();
            if (!folderCreated) {
                log.warn("Could not create folder {}", targetFile.getAbsolutePath());
                return null;
            }
        }
        String fileExtension = getFileExtension(Objects.requireNonNull(sourceFile.getOriginalFilename()));
        String targetFilePath = finalUploadPath + File.separator + currentTimeMillis() + "." + fileExtension;

        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("file has been saved to {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("unable to save file {}", e.getMessage());
        }
        return null;
    }

    public byte[] downloadFile(@NonNull String filePath) {
        return FileUtils.readFileFromLocation(filePath);
    }

    private String getFileExtension(@NonNull String originalFilename) {
        if (originalFilename.isBlank() || originalFilename.isEmpty()) {
            return null;
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}
