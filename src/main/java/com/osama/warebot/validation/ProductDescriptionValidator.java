package com.osama.warebot.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

public class ProductDescriptionValidator implements ConstraintValidator<ValidProductDescription, Set<Map<String, Map<String, String>>>> {

    private Class<?>[] groups;

    @Override
    public void initialize(ValidProductDescription constraintAnnotation) {
        this.groups = constraintAnnotation.groups();
    }

    @Override
    public boolean isValid(Set<Map<String, Map<String, String>>> descriptions,
                           ConstraintValidatorContext context) {
        boolean strictMode = groupsContain("OnCreate");

        if (descriptions == null) {
            return !strictMode;
        }
        if (descriptions.isEmpty()) {
            return false;
        }

        boolean foundValidField = false;

        for (Map<String, Map<String, String>> attribute : descriptions) {
            if (attribute == null || attribute.size() != 1) {
                continue;
            }

            for (Map.Entry<String, Map<String, String>> entry : attribute.entrySet()) {
                String attributeName = entry.getKey();
                Map<String, String> localizedValues = entry.getValue();
                if (attributeName == null || attributeName.isBlank()) {
                    continue;
                }
                if (localizedValues == null) {
                    continue;
                }
                boolean hasValidValue = false;
                for (String value : localizedValues.values()) {
                    if (value != null && !value.isBlank()) {
                        hasValidValue = true;
                        break;
                    }
                }

                if (hasValidValue) {
                    foundValidField = true;
                }

                if (!LocalizedValidationHelper.isValidLocalizedMap(localizedValues, strictMode)) {
                    return false;
                }
            }
        }

        return foundValidField;
    }

    private boolean groupsContain(String name) {
        if (groups == null) return false;
        for (Class<?> group : groups) {
            if (group.getSimpleName().equals(name)) return true;
        }
        return false;
    }
}