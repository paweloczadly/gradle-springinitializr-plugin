package io.oczadly.internal.utils

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification

class InputValidatorSpec extends Specification {

    def 'sanitize returns fallback value for null or empty input'() {
        expect:
        InputValidator.sanitize(null, 'java') == 'java'
        InputValidator.sanitize('', 'java') == 'java'
        InputValidator.sanitize('   ', 'java') == 'java'
        InputValidator.sanitize('null', 'java') == 'java'
    }

    def 'sanitize trims input'() {
        expect:
        InputValidator.sanitize('  kotlin  ', 'java') == 'kotlin'
    }

    def 'validateSupportedValues throws exception for unsupported value'() {
        when:
        InputValidator.validateSupportedValues 'scala', ['groovy', 'java', 'kotlin'], 'language'

        then:
        thrown InvalidUserDataException
    }

    def 'validateSupportedValues does not throw for supported value'() {
        when:
        InputValidator.validateSupportedValues 'groovy', ['groovy', 'java', 'kotlin'], 'language'

        then:
        noExceptionThrown()
    }

    def 'validateSupportedExtractValues throws exception for invalid extract value'() {
        when:
        InputValidator.validateSupportedExtractValues 'maybe'

        then:
        thrown InvalidUserDataException
    }

    def 'validateSupportedExtractValues does not throw for valid extract values'() {
        when:
        InputValidator.validateSupportedExtractValues 'true'
        InputValidator.validateSupportedExtractValues 'false'

        then:
        noExceptionThrown()
    }
}
