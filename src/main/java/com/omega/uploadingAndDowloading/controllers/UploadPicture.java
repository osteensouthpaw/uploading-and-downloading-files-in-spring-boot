package com.omega.uploadingAndDowloading.controllers;

import com.omega.uploadingAndDowloading.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UploadPicture {
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPicture(@RequestPart("file") MultipartFile file) {
        if (!file.getContentType().equalsIgnoreCase("image/jpeg")) {
            return ResponseEntity.badRequest().body("only image files are allowed");
        }
        fileStorageService.saveFile(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/download", produces = "image/png")
    public byte[] downloadPicture() {
        String path = "uploads/products/categories/1715277514231.png";
        return fileStorageService.downloadFile(path);
    }
}
