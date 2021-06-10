package com.example.datstestprj.validator;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Service
public class CurrencyRateDateOnValidator implements ConstraintValidator<CurrencyRateDateOn, LocalDate> {

    @Override
    public boolean isValid(LocalDate dateOn, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOn.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }
}
