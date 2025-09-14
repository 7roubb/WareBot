package com.osama.warebot.validation;

import java.util.Map;

public class LocalizedValidationHelper {

    public static boolean isValidLocalizedMap(Map<String, String> map, boolean strictValidation) {
        if (map == null) {
            return !strictValidation;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key == null || key.isBlank()) {
                return false;
            }

            if (strictValidation && (value == null || value.isBlank())) {
                return false;
            }
        }

        return true;
    }
}