package io.oczadly.internal.utils

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class BootVersionUtilsSpec extends Specification {

    @Unroll
    def 'sanitize accepts #version'() {
        expect:
        BootVersionUtils.sanitize version

        where:
        version << ['3.4.7',
                    '3.4.7-M1',
                    '3.4.7-SNAPSHOT',]
    }

    @Unroll
    def 'sanitize rejects #version'() {
        when:
        BootVersionUtils.sanitize version

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message == "Invalid Spring Boot version '$version'. Expected x.y.z, x.y.z-M<N>, or x.y.z-SNAPSHOT."

        where:
        version << [null,
                    '',
                    '3.4',
                    '3.4.x',
                    'abc']
    }

    @Unroll
    def 'toInitializr maps #input -> #output'() {
        expect:
        BootVersionUtils.toInitializr(input) == output

        where:
        input            || output
        null             || null
        '3.4.7'          || '3.4.7.RELEASE'
        '3.4.7-SNAPSHOT' || '3.4.7.BUILD-SNAPSHOT'
        '3.4.7-M1'       || '3.4.7.M1'
    }

    @Unroll
    def 'toRequestBootVersion maps #input -> #output'() {
        expect:
        BootVersionUtils.toRequestBootVersion(input) == output

        where:
        input                  || output
        null                   || null
        '3.4.7.RELEASE'        || '3.4.7'
        '3.4.7.BUILD-SNAPSHOT' || '3.4.7-SNAPSHOT'
        '3.4.7.M1'             || '3.4.7-M1'
    }
}
