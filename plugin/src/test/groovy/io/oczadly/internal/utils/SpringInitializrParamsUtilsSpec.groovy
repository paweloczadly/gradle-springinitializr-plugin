package io.oczadly.internal.utils

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class SpringInitializrParamsUtilsSpec extends Specification {

    Map<String, List<String>> supportedOptions

    def setup() {
        supportedOptions = [type    : ['gradle-project', 'gradle-project-kotlin', 'maven-project'],
                            language: ['java', 'kotlin', 'groovy']]
    }

    @Unroll
    def 'allowedOption returns normalized value on happy path for #field'() {
        expect:
        SpringInitializrParamsUtils.allowedOption(field, raw, [(field): supportedOptions[field]]) == expected

        where:
        field         || raw               || expected
        'language'    || ' KOTLIN'         || 'kotlin'
        'type'        || ' Maven-Project ' || 'maven-project'
    }

    @Unroll
    def 'allowedOption throws with clear message for unsupported #field=#raw'() {
        given:
        def prettyField = field.replaceAll(/([A-Z])/, ' $1').toLowerCase(Locale.ROOT)

        when:
        SpringInitializrParamsUtils.allowedOption field, raw, [(field): supportedOptions[field]]

        then:
        def ex = thrown(InvalidUserDataException)
        ex.message == "Unsupported $prettyField: '${raw.toString().trim().toLowerCase(Locale.ROOT)}'. Supported: ${supportedOptions[field].join(', ')}."

        where:
        field         || raw
        'type'        || 'sbt'
        'language'    || 'scala'
    }

    @Unroll
    def 'allowedOption returns null for #value'() {
        expect:
        SpringInitializrParamsUtils.allowedOption('type', value, supportedOptions) == null
        SpringInitializrParamsUtils.allowedOption('language', value, supportedOptions) == null

        where:
        value << [null,
                  'null',
                  '',]
    }

    @Unroll
    def 'text trims #input -> #expected'() {
        expect:
        SpringInitializrParamsUtils.text(input) == expected

        where:
        input  || expected
        ' a'   || 'a'
        'a '   || 'a'
        ' a '  || 'a'
        null   || null
        'null' || null
        ''     || null
    }

    @Unroll
    def 'hasText(#input) -> #expected'() {
        expect:
        SpringInitializrParamsUtils.hasText(input) == expected

        where:
        input || expected
        null  || false
        ''    || false
        '   ' || false
        ' a ' || true
        123   || true
        ['x'] || true
    }
}
