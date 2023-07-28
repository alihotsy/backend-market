package com.zapp.marketapp.interfaces;

import com.zapp.marketapp.utils.FileResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUpload {

    FileResponse uploadFile(Integer id, String type, MultipartFile file) throws Exception;
    void getImage(String type, String image, HttpServletResponse response) throws IOException;

}
