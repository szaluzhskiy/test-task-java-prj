package com.example.datstestprj.resource;

import com.example.datstestprj.dto.ErrorDto;
import com.example.datstestprj.exception.WrongCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final Map<Class<? extends Exception>, String> codes = Map.of(
            WrongCurrencyException.class, "illegalArgumentError",
            ConstraintViolationException.class, "illegalArgumentError"
    );

    @ExceptionHandler(value = {WrongCurrencyException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> notFoundHandle(RuntimeException ex, WebRequest request) {
        log.warn(ex.getMessage());
        ErrorDto error = ErrorDto.builder()
                .code(codes.get(ex.getClass()))
                .details(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
