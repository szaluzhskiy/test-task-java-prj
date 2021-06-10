package com.example.datstestprj.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;
import java.util.function.Supplier;

public class WrongCurrencyException extends RuntimeException {

    public WrongCurrencyException(String currency) {
        super(String.format("Выбрана неприменимая валюта %s", currency));
    }

    public static Supplier<WrongCurrencyException> wrongCurrencySupplied(String currency) {
        return () -> new WrongCurrencyException(currency);
    }
}
