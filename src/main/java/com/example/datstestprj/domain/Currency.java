package com.example.datstestprj.domain;

import com.example.datstestprj.types.CurrencyType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "currency_rate", uniqueConstraints = { @UniqueConstraint(columnNames =
        { "source_currency", "base_currency", "date_on" }) })
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Currency {
    @Id
    @GeneratedValue
    UUID id;
    @Column(name="source_currency")
    CurrencyType sourceCurrency;
    @Column(name="base_currency")
    CurrencyType baseCurrency;
    @Column(name="rate")
    Double rate;
    @Column(name="date_on")
    LocalDate dateOn;
}
