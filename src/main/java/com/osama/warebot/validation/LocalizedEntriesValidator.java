package com.osama.warebot.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;

public class LocalizedEntriesValidator implements ConstraintValidator<ValidLocalizedEntries, Map<String, String>> {

    private Class<?>[] groups;

    @Override
    public void initialize(ValidLocalizedEntries constraintAnnotation) {
        this.groups = constraintAnnotation.groups();
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        boolean strictMode = groupsContain("OnCreate");
        return LocalizedValidationHelper.isValidLocalizedMap(value, strictMode);
    }

    private boolean groupsContain(String name) {
        if (groups == null) return false;
        for (Class<?> group : groups) {
            if (group.getSimpleName().equals(name)) return true;
        }
        return false;
    }
}