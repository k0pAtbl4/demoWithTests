package com.example.demowithtests.util.annotations.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BlockedEmailDomainsValidator.class)
public @interface BlockedEmailDomains {

    String message() default "Русский военный корабль, пошел нах@й";

    String[] contains() default {"ru", "su", "@yandex", "@mail", "@rumbler"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
