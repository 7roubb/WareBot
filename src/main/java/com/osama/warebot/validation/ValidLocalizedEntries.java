package com.osama.warebot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalizedEntriesValidator.class)
public @interface ValidLocalizedEntries {
    String message() default "{localized.entry.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
