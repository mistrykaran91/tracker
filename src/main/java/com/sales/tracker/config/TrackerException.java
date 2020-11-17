package com.sales.tracker.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TrackerException {

    private Map<String, String> errorMap = new HashMap<>();

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        String message = ex.getCause().getCause().getMessage();

        if (message.contains("already exists")) {
            message = "Duplicate data cannot be inserted";
        }
        this.errorMap.put("error", message);
        return new ResponseEntity<Object>(this.errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
