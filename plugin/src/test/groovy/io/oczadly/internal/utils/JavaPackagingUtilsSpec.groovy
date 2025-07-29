package io.oczadly.internal.utils

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class JavaPackagingUtilsSpec extends Specification {

    @Unroll
    def 'valid package #value'() {
        when:
        JavaPackagingUtils.validateJavaPackagingConvention value, 'groupId'

        then:
        noExceptionThrown()

        where:
        value << ['io.oczadly',
                  'c0m.example1',
                  'a.b',]
    }

    @Unroll
    def 'invalid package #value'() {
        when:
        JavaPackagingUtils.validateJavaPackagingConvention value, 'packageName'

        then:
        thrown InvalidUserDataException

        where:
        value << [null,
                  '',
                  '.invalid',
                  'invalid..name',
                  'Invalid.Case',
                  'with-dash',
                  '1bad.start',]
    }
}
