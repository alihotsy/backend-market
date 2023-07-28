package com.zapp.marketapp.exceptions.handler;

import com.google.gson.stream.MalformedJsonException;
import com.zapp.marketapp.exceptions.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControlled {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> badArguments(MethodArgumentNotValidException e) {
        Map<String,Object> errors = new LinkedHashMap<>();
        errors.put("ok",false);

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName,error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityException.class)
    public ResponseEntity<EntityExceptionCaught> productException(EntityException e) {
        EntityExceptionCaught exception = new EntityExceptionCaught(false,e.getSource(),e.getMsg(), e.getField(), e.getStatus());
        final HttpStatus status = switch (e.getStatus()) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 404 -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.UNAUTHORIZED;
        };

        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String,Object>> userNotFound(UsernameNotFoundException e) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg",e.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> accessDenied(AccessDeniedException e) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg","No tiene permisos para realizar esta acción");
        return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GoogleTokenExceptionCaught> googleTokenException(IllegalArgumentException e) {
        GoogleTokenExceptionCaught exception = new GoogleTokenExceptionCaught();
        exception.setOk(false);
        exception.setMsg("Token de Google inválido");
        exception.setSource("GOOGLE_TOKEN");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJsonException.class)
    public ResponseEntity<Map<String,Object>> malformedJsonException(MalformedJsonException e) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg","El token está mal formado");
        resp.put("source", "GOOGLE_TOKEN");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<FileExceptionCaught> fileException (FileException e) {
        HttpStatus status = e.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
        FileExceptionCaught exception = new FileExceptionCaught(false, e.getMsg(),"file");
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<TypeExceptionCaught> typeException (InvalidTypeException e) {
        System.out.println(e.getMsg());
        TypeExceptionCaught exception = new TypeExceptionCaught(false, e.getMsg(),"file");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> sizeExceededFile(MaxUploadSizeExceededException e) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg", "El tamaño de la imagen excede a los 5MB");
        return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> globalException(Exception e) {
        System.out.println(e.getClass().getName());
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg", e.getMessage());
        return new ResponseEntity<>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> runtimeException(RuntimeException e) {
        System.out.println(e.getClass().getName());
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("ok",false);
        resp.put("msg", e.getMessage());
        return new ResponseEntity<>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
