package com.example.datstestprj.exception;

import java.util.function.Supplier;

public class WrongDateOnException extends RuntimeException {

    public WrongDateOnException(String currency) {
        super(String.format("Запрос содержит неверную дату %s", currency));
    }

    public static Supplier<WrongDateOnException> wrongCurrencySupplied(String currency) {
        return () -> new WrongDateOnException(currency);
    }
}
