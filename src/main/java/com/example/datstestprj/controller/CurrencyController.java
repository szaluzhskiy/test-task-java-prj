package com.example.datstestprj.controller;

import com.example.datstestprj.resource.CurrencyResource;
import com.example.datstestprj.services.CBRFCurrencyExchangeService;
import com.example.datstestprj.types.CurrencyType;
import com.example.datstestprj.validator.CurrencyRateDateOn;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

@RestController("/api/currency")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CurrencyController implements CurrencyResource {

    CBRFCurrencyExchangeService cbrfCurrencyExchangeService;

    @Override
    @Validated
    public String getRateForCurrencyByDate(CurrencyType currencyType, @CurrencyRateDateOn LocalDate dateOn) {
        return cbrfCurrencyExchangeService.getExchangeRate(currencyType, dateOn);
    }

    @Override
    @Validated
    public void loadRateForCurrencyByDate(@CurrencyRateDateOn LocalDate dateOn) {
        try {
            cbrfCurrencyExchangeService.parse(dateOn);
        } catch (IOException | InterruptedException | SAXException | ParserConfigurationException | XPathExpressionException | ParseException e) {
            log.error(e.getMessage(), e);
        }
    }
}
