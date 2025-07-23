package io.oczadly.internal.generator

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class SpringInitializrParamsBuilderSpec extends Specification {

    Map<String, List<String>> supportedOptions

    def setup() {
        supportedOptions = [type    : ['gradle-project', 'gradle-project-kotlin', 'maven-project'],
                            language: ['java', 'kotlin', 'groovy']]
    }

    def 'buildQueryParams creates full path'() {
        given:
        Map<String, Object> params = [type: ' Gradle-Project ', language: ' JAVA ']

        when:
        LinkedHashMap<String, String> query = SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        query.type == 'gradle-project'
        query.language == 'java'
    }

    def 'buildQueryParams passes through plain fields without validation lists'() {
        given:
        Map<String, Object> params = [type: '  ', language: 'java', foo: ' bar ']
        Map<String, List<String>> lists = [:]

        when:
        def out = SpringInitializrParamsBuilder.buildQueryParams(params, lists)

        then:
        !out.containsKey('type')
        out.language == 'java'
        !out.containsKey('foo')
    }

    @Unroll
    def 'buildQueryParams rejects unsupported #key=#value'() {
        given:
        Map<String, Object> params = [:]
        params[key] = value
        def prettyField = key.replaceAll(/([A-Z])/, ' $1').toLowerCase(Locale.ROOT)

        when:
        SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message == "Unsupported $prettyField: '$value'. Supported: ${supportedOptions[key].join(', ')}."

        where:
        key           || value
        'type'        || 'sbt'
        'language'    || 'scala'
    }
}
