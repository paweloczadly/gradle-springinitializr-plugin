package io.oczadly.internal.utils

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException

@CompileStatic
class InputValidator {

    static String sanitize(String input, String fallback) {
        (input == null || input.trim() == '' || input.trim().equalsIgnoreCase('null')) ? fallback : input.trim()
    }

    static void validateSupportedValues(String value, List<String> supportedValues, String label) {
        if (!supportedValues.contains(value)) {
            throw new InvalidUserDataException("Unsupported $label: '$value'. Supported options: ${supportedValues.toSorted()}")
        }
    }
}
