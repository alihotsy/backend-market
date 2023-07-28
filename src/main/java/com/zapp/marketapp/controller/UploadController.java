package com.zapp.marketapp.controller;

import com.zapp.marketapp.exceptions.FileException;
import com.zapp.marketapp.exceptions.InvalidTypeException;
import com.zapp.marketapp.service.UploadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class UploadController {

    private final UploadService uploadService;

    @Secured({"ADMIN_ROLE","INVENTORY_ROLE"})
    @PutMapping("/update/{type}/{id}")
    public ResponseEntity<Object> uploadImage(
            @PathVariable String type,
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile file
            ) throws Exception {


        return ResponseEntity.ok(uploadService.uploadFile(id,type,file));
    }


    @GetMapping(value = "/{type}/{image}",produces = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE})
    public void getImage(
            @PathVariable String type,
            @PathVariable String image,
            HttpServletResponse response
    ) throws IOException {
        uploadService.getImage(type,image,response);
    }
}
