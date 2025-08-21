package io.oczadly.internal.utils

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class SpringInitializrParamsUtilsSpec extends Specification {

    Map<String, List<String>> supportedOptions

    def setup() {
        supportedOptions = [type        : ['gradle-project', 'gradle-project-kotlin', 'maven-project'],
                            language    : ['java', 'kotlin', 'groovy'],
                            bootVersion : ['3.4.7.BUILD-SNAPSHOT', '3.4.7.M99', '3.4.7.RELEASE'],
                            packaging   : ['jar', 'war'],
                            javaVersion : ['24', '21', '17'],
                            dependencies: ['web', 'actuator', 'data-jpa']]
    }

    @Unroll
    def 'allowedOption returns normalized value on happy path for #field'() {
        expect:
        SpringInitializrParamsUtils.allowedOption(field, raw, [(field): supportedOptions[field]]) == expected

        where:
        field         || raw               || expected
        'language'    || ' KOTLIN'         || 'kotlin'
        'type'        || ' Maven-Project ' || 'maven-project'
        'packaging'   || 'JAR'             || 'jar'
        'javaVersion' || '17'              || '17'
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
        'packaging'   || 'tar.gz'
        'javaVersion' || '8'
    }

    @Unroll
    def 'allowedOption returns null for #value'() {
        expect:
        SpringInitializrParamsUtils.allowedOption('type', value, supportedOptions) == null
        SpringInitializrParamsUtils.allowedOption('language', value, supportedOptions) == null
        SpringInitializrParamsUtils.allowedOption('packaging', value, supportedOptions) == null
        SpringInitializrParamsUtils.allowedOption('javaVersion', value, supportedOptions) == null

        where:
        value << [null,
                  'null',
                  '',]
    }

    @Unroll
    def 'bootVersion accepts supported Spring Boot version #userInput'() {
        expect:
        SpringInitializrParamsUtils.bootVersion(userInput, supportedOptions['bootVersion']) == bootVersion

        where:
        userInput              || bootVersion
        '3.4.7.RELEASE'        || '3.4.7'
        '3.4.7'                || '3.4.7'
        '3.4.7.M99'            || '3.4.7-M99'
        '3.4.7-M99'            || '3.4.7-M99'
        '3.4.7.BUILD-SNAPSHOT' || '3.4.7-SNAPSHOT'
        '3.4.7-SNAPSHOT'       || '3.4.7-SNAPSHOT'
    }

    @Unroll
    def 'bootVersion throws InvalidUserDataException for unsupported #value'() {
        when:
        SpringInitializrParamsUtils.bootVersion value, supportedOptions['bootVersion']

        then:
        def ex = thrown InvalidUserDataException
        ex.message == "Unsupported Spring Boot version: '$value'. Supported: ${supportedOptions['bootVersion'].join(', ')}."

        where:
        value << ['0.0.1.BUILD-SNAPSHOT',
                  '0.0.1.M1',
                  '0.0.1.RELEASE']
    }

    @Unroll
    def 'bootVersion returns null for #value'() {
        expect:
        SpringInitializrParamsUtils.bootVersion(value, supportedOptions['bootVersion']) == null

        where:
        value << [null,
                  'null',
                  '',]
    }

    @Unroll
    def 'dependencies returns normalized value on happy path for #input'() {
        expect:
        SpringInitializrParamsUtils.dependencies(input, supportedOptions['dependencies']) == expected

        where:
        input             || expected
        ' WEB, Actuator ' || 'web,actuator'
        'data-jpa,web'    || 'data-jpa,web'

    }

    @Unroll
    def 'dependencies throws InvalidUserDataException for unsupported #value'() {
        when:
        SpringInitializrParamsUtils.dependencies value, supportedOptions['dependencies']

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message == "Unsupported dependency: '$value'. Supported: ${supportedOptions['dependencies'].join(', ')}."

        where:
        value << ['ai',
                  'akka',
                  'metabase']
    }

    @Unroll
    def 'dependencies returns null for #value'() {
        given:
        def allowed = ['web', 'actuator', 'data-jpa']

        expect:
        SpringInitializrParamsUtils.dependencies(value, allowed) == null

        where:
        value << [null,
                  'null',
                  '',
                  ' ',
                  ',',
                  ' , ',
                  ' , , ',
                  [],]
    }

    def 'javaPackage delegates and trims'() {
        expect:
        SpringInitializrParamsUtils.javaPackage(' io.oczadly ', 'groupId') == 'io.oczadly'
    }

    @Unroll
    def 'javaPackage returns null for #value'() {
        expect:
        SpringInitializrParamsUtils.javaPackage(value, 'groupId') == null
        SpringInitializrParamsUtils.javaPackage(value, 'packageName') == null

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
