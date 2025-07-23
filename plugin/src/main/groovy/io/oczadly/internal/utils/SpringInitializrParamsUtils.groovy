package io.oczadly.internal.utils

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException

@CompileStatic
class SpringInitializrParamsUtils {

    static String allowedOption(String field, Object raw, Map<String, List<String>> lists) {
        String stringValue = text raw
        if (!stringValue) {
            return null
        }
        String val = stringValue.toLowerCase Locale.ROOT

        List<String> allowed = lists?.get field
        if (allowed && !allowed.contains(val)) {
            String prettyField = field.replaceAll(/([A-Z])/, ' $1').toLowerCase(Locale.ROOT)
            throw new InvalidUserDataException("Unsupported $prettyField: '$val'. Supported: ${allowed.join(', ')}.")
        }
        val
    }

    static String text(Object v) {
        hasText(v) ? v.toString().trim() : null
    }

    static boolean hasText(Object raw) {
        if (!raw) {
            return false
        }
        if (raw instanceof CharSequence) {
            String stringValue = raw.toString().trim()
            return stringValue.length() > 0 && !stringValue.equalsIgnoreCase('null')
        }
        true
    }
}
