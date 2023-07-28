package com.zapp.marketapp.service;

import com.zapp.marketapp.entities.User;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.exceptions.FileException;
import com.zapp.marketapp.exceptions.InvalidTypeException;
import com.zapp.marketapp.helpers.SaveImage;
import com.zapp.marketapp.interfaces.FileUpload;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.FileResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadService implements FileUpload {

    //private final static long MAX_SIZE = 5242880;
    private final static List<String> types = Arrays.asList("users", "products", "categories");
    private final SaveImage saveImage;
    @Override
    public FileResponse uploadFile(Integer id, String type, MultipartFile file) throws Exception {

        if(file.isEmpty()) {
            throw new FileException("No se encontró ninguna imagen!",400);
        }

        List<String> extensions = Arrays.asList("png","jpg","jpeg");
        String[] dividedName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String ext = dividedName[dividedName.length - 1];

        if(!extensions.contains(ext)) {
            throw new FileException("La extensión no es válida. Solo permite png,jpg o jpeg",400);
        }

        if(!types.contains(type)) {
            throw new InvalidTypeException("Tipo no válido. Solo se permite " + types);
        }

        Path directoryImage = Paths.get("src//main//resources//static//images/"+type); //Ruta relativa
        String absolutePath = directoryImage.toFile().getAbsolutePath(); //Ruta absoluta

        byte[] imgByte = file.getBytes();
        String newImage = UUID.randomUUID()+"."+ext;


        String getImageSaved = saveImage.getImageSaved(type,absolutePath,newImage,id);
        Path completePath = Paths.get(absolutePath + "//"+ getImageSaved);
        String getNameImageSaved = Files.write(completePath,imgByte).toFile().getName();

        return FileResponse.builder()
                .ok(true)
                .file(getNameImageSaved)
                .file_type(ext)
                .location(type)
                .build();
    }

    @Override
    public void getImage(String type, String image, HttpServletResponse response) throws IOException {

        if(!types.contains(type)) {
            throw new FileException("El tipo no es válido, solo se permite: "+types,400);
        }

        String[] imageDivided = image.split("\\.");
        String ext = imageDivided[imageDivided.length - 1];

        Path imageDirectory = Paths.get("src//main//resources//static/images");
        String absolutePath = imageDirectory.toFile().getAbsolutePath();

        Path imagePath = Paths.get(absolutePath+"//"+type+"//"+image);

        String imageFormat = MediaType.IMAGE_PNG_VALUE;
        if(Files.notExists(imagePath)) {
            imagePath = Paths.get(absolutePath+"//"+"no-image.png");
            System.out.println("Imagen no existe");
            response.setContentType(imageFormat);
        }else {
            boolean isJpg = ext.equals("jpg") || ext.equals("jpeg");
            imageFormat = isJpg ? MediaType.IMAGE_JPEG_VALUE
                                : MediaType.IMAGE_PNG_VALUE;
            response.setContentType(imageFormat);
        }

        var imageFile = new FileInputStream(imagePath.toFile().getPath());
        StreamUtils.copy(imageFile, response.getOutputStream());

    }
}
