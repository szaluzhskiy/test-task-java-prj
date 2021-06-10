package com.example.datstestprj.resource;

import com.example.datstestprj.types.CurrencyType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


public interface CurrencyResource {

    @Operation(summary = "API метод курса валюты на определенную дату")
    @GetMapping("/{currencyType}")
    String getRateForCurrencyByDate(@PathVariable(name = "currencyType", required = true) CurrencyType currencyType,
                                    @RequestParam(name = "dateOn", required = true)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOn);

    @Operation(summary = "API метод для загрузки курсов валют из ЦБ РФ на определенную дату в сервис")
    @PostMapping
    void loadRateForCurrencyByDate(@RequestParam(name = "dateOn", required = true)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOn);
}
