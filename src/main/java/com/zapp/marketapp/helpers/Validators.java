package com.zapp.marketapp.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Validators {

    private final ObjectMapper mapper;

    public void jsonError(String msg, HttpServletResponse response) throws IOException {
        Map<String,Object> resp = new LinkedHashMap<>();

        resp.put("ok",false);
        resp.put("msg",msg);
        resp.put("source","JWT");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(),resp);
    }
}
