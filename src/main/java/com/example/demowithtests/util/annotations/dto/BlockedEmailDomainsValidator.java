package com.example.demowithtests.util.annotations.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BlockedEmailDomainsValidator implements ConstraintValidator<BlockedEmailDomains, String> {

    private String[] domains;

    @Override
    public void initialize(BlockedEmailDomains blockedEmailDomains) {
        ConstraintValidator.super.initialize(blockedEmailDomains);
        domains = blockedEmailDomains.contains();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email == null) {
            return false;
        }
        return Arrays.stream(domains).noneMatch(email::contains);
    }
}
