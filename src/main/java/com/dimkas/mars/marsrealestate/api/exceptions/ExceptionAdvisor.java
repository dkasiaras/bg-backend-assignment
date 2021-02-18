package com.dimkas.mars.marsrealestate.api.exceptions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = "com.dimkas.mars.marsrealestate")
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", status.value());
        body.put("exceptionType", "Input validation exception");

        List<Map<String, String>> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(x -> {
                                   Map<String,String > map = new HashMap<>();
                                   map.put(x.getField(), x.getDefaultMessage());
                                   return map;
                                })
                                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleExceptionInternal(Exception ex) {
        ex.printStackTrace();
        Map<String, Object> bodyPayload = new LinkedHashMap<>();
        bodyPayload.put("timestamp", LocalDate.now());
        bodyPayload.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        bodyPayload.put("exceptionType", "generic exception");
        bodyPayload.put("exception", ex);

        return new ResponseEntity<>(bodyPayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
