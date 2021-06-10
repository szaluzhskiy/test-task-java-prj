package com.example.datstestprj.repository;

import com.example.datstestprj.domain.Currency;
import com.example.datstestprj.types.CurrencyType;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface CurrencyRepository extends CrudRepository<Currency,String> {
    public Currency findCurrencyBySourceCurrencyAndDateOnEquals(CurrencyType currencyType, LocalDate dateOn);
}
