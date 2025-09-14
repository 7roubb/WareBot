package com.osama.warebot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductDescriptionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductDescription {
    String message() default "Invalid product description attributes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}