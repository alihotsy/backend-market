package com.zapp.marketapp.exceptions.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImp implements AccessDeniedHandler {

    private final ObjectMapper mapper;


    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
         Map<String,Object> resp = new LinkedHashMap<>();
         resp.put("ok",false);
         resp.put("msg", "No tiene permisos para realizar esta acción!");

         response.setStatus(HttpStatus.UNAUTHORIZED.value());
         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         mapper.writeValue(response.getWriter(),resp);
    }
}
