package com.example.demowithtests.util.annotations.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;

public class CountryRightFormedValidator implements ConstraintValidator<CountryRightFormed, String> {

    private final Set<String> isoCountries = Set.of(Locale.getISOCountries());

    @Override
    public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
        if (country == null)
            return false;
        return isoCountries.contains(country);
    }
}
