package io.oczadly.internal.utils

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException

@CompileStatic
class JavaPackagingUtils {

    private static final String PKG_SEG = '[a-z][a-z0-9]*'
    private static final String PKG_REGEX = "^${PKG_SEG}(\\.${PKG_SEG})*\$"

    static void validateJavaPackagingConvention(String value, String label) {
        if (value == null || !(value =~ PKG_REGEX)) {
            String prettyField = label.replaceAll(/([A-Z])/, ' $1').toLowerCase(Locale.ROOT)
            throw new InvalidUserDataException("Invalid $prettyField: '$value'. Must be a valid Java package (e.g. com.example).")
        }
    }
}
