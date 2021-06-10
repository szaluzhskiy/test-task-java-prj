package com.example.datstestprj.resource;

import com.example.datstestprj.dto.ErrorDto;
import com.example.datstestprj.exception.WrongCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
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

    @ExceptionHandler(value = {WrongCurrencyException.class, ConstraintViolationException.class,
            javax.validation.ConstraintViolationException.class})
    protected ResponseEntity<Object> badRequestHandle(RuntimeException ex, WebRequest request) {
        log.warn(ex.getMessage());
        ErrorDto error = ErrorDto.builder()
                .code(codes.get(ex.getClass()))
                .details(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
