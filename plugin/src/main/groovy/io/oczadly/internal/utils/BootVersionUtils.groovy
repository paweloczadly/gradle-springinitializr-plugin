package io.oczadly.internal.utils

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException

import java.util.regex.Matcher
import java.util.regex.Pattern

@CompileStatic
class BootVersionUtils {

    private static final String CORE = '\\d+\\.\\d+\\.\\d+'
    private static final String REGEX = "^${CORE}(?:-(?:M\\d+|SNAPSHOT))?\$"

    private static final Pattern SNAPSHOT_DASH = Pattern.compile('-SNAPSHOT$')
    private static final Pattern SNAPSHOT_DOT = Pattern.compile('\\.BUILD-SNAPSHOT$')
    private static final Pattern MILESTONE_DASH = Pattern.compile('-(M\\d+)$')
    private static final Pattern MILESTONE_DOT = Pattern.compile('\\.(M\\d+)$')

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

        String out = replaceIfMatches(version, SNAPSHOT_DASH, '.BUILD-SNAPSHOT')
        if (out != null) {
            return out
        }

        out = replaceIfMatches(version, MILESTONE_DASH, '.$1')
        return out != null ? out : version + '.RELEASE'
    }

    static String toRequestBootVersion(String version) {
        if (version == null) {
            return null
        }

        String out = replaceIfMatches(version, SNAPSHOT_DOT, '-SNAPSHOT')
        if (out != null) {
            return out
        }

        out = replaceIfMatches(version, MILESTONE_DOT, '-$1')
        return out != null ? out : stripSuffix(version, '.RELEASE')
    }

    private static String replaceIfMatches(String s, Pattern pattern, String replacement) {
        Matcher m = pattern.matcher(s)
        m.find() ? m.replaceFirst(replacement) : null
    }

    private static String stripSuffix(String s, String suffix) {
        s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s
    }
}
