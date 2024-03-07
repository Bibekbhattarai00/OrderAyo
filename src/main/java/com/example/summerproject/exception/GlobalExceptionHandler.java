package com.example.summerproject.exception;

import com.example.summerproject.geneericresponse.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final CustomMessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponse<Map<String, String>> invalidArgumentHandler(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
        return GenericResponse.<Map<String, String>>builder()
                .success(false)
                .message(messageSource.get(ExceptionMessages.METHOD_INVALID.getCode()))
                .data(map)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public GenericResponse<Map<String, String>> userNotFoundException(NotFoundException e) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", e.message);
        return GenericResponse.<Map<String, String>>builder()
                .success(false)
                .message(messageSource.get(ExceptionMessages.NOT_FOUND_EXCEPTION.getCode()))
                .data(map)
                .build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public GenericResponse<Map<String, String>> globalException(Exception e) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", e.getMessage());
        return GenericResponse.<Map<String, String>>builder()
                .success(false)
                .message(messageSource.get(ExceptionMessages.EXCEPTION.getCode()))
                .data(map)
                .build();
    }
}
