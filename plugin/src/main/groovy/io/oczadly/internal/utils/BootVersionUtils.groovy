package io.oczadly.internal.utils

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException

import java.util.regex.Matcher

@CompileStatic
class BootVersionUtils {

    private static final String CORE = '\\d+\\.\\d+\\.\\d+'
    private static final String REGEX = "^${CORE}(?:-(?:M\\d+|SNAPSHOT))?\$"
    private static final String MILESTONE = /-(M\d+)$/

    static String sanitize(String version) {
        String v = version == null ? null : version.trim()
        if (v != null && v.matches(REGEX)) {
            return v
        }
        throw new InvalidUserDataException("Invalid Spring Boot version '$version'. Expected x.y.z, x.y.z-M<N>, or x.y.z-SNAPSHOT.")
    }

    static String toInitializr(String version) {
        if (version == null) {
            return null
        }
        if (version.endsWith('-SNAPSHOT')) {
            return version.replace('-SNAPSHOT', '.BUILD-SNAPSHOT')
        }

        Matcher matcher = (version =~ MILESTONE)
        if (matcher.find()) {
            return version.replaceFirst(MILESTONE, '.' + matcher.group(1))
        }

        version + '.RELEASE'
    }
}
