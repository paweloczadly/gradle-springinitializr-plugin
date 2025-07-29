package io.oczadly.internal.generator

import org.gradle.api.InvalidUserDataException
import spock.lang.Specification
import spock.lang.Unroll

class SpringInitializrParamsBuilderSpec extends Specification {

    Map<String, List<String>> supportedOptions

    def setup() {
        supportedOptions = [type        : ['gradle-project', 'gradle-project-kotlin', 'maven-project'],
                            language    : ['java', 'kotlin', 'groovy'],
                            bootVersion : ['3.4.7-SNAPSHOT', '3.4.7-M1', '3.4.7'],
                            packaging   : ['jar', 'war'],
                            javaVersion : ['24', '21', '17'],
                            dependencies: ['web', 'actuator', 'data-jpa']]
    }

    def 'buildQueryParams creates full path'() {
        given:
        Map<String, Object> params = [type        : ' Gradle-Project ',
                                      language    : ' JAVA ',
                                      bootVersion : '3.4.7-SNAPSHOT',
                                      groupId     : ' io.oczadly ',
                                      artifactId  : ' demo ',
                                      name        : ' Demo App ',
                                      description : ' My demo ',
                                      packageName : ' io.oczadly.demo ',
                                      packaging   : ' jar ',
                                      javaVersion : ' 21 ',
                                      dependencies: ' Web , actuator, WEB ']

        when:
        LinkedHashMap<String, String> query = SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        query.type == 'gradle-project'
        query.language == 'java'
        query.bootVersion == '3.4.7.BUILD-SNAPSHOT'
        query.groupId == 'io.oczadly'
        query.artifactId == 'demo'
        query.name == 'Demo App'
        query.description == 'My demo'
        query.packageName == 'io.oczadly.demo'
        query.packaging == 'jar'
        query.javaVersion == '21'
        query.dependencies == 'web,actuator,web'
    }

    def 'buildQueryParams passes through plain fields without validation lists'() {
        given:
        Map<String, Object> params = [artifactId : ' demo ',
                                      name       : '  ',
                                      foo        : ' bar ',
                                      bootVersion: null]
        Map<String, List<String>> lists = [:]

        when:
        def out = SpringInitializrParamsBuilder.buildQueryParams(params, lists)

        then:
        out.artifactId == 'demo'
        !out.containsKey('name')
        !out.containsKey('foo')
        !out.containsKey('bootVersion')
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
        'packaging'   || 'tar.gz'
        'javaVersion' || '8'
    }

    @Unroll
    def 'buildQueryParams rejects unsupported bootVersion=#value'() {
        given:
        Map<String, Object> params = [bootVersion: value]

        when:
        SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message.contains "Unsupported Spring Boot version: '$value'. Supported: ${supportedOptions['bootVersion'].join(', ')}"

        where:
        value << ['0.0.1-SNAPSHOT',
                  '0.0.1-M1',
                  '0.0.1']
    }

    @Unroll
    def 'buildQueryParams rejects unsupported dependency=#value'() {
        given:
        Map<String, Object> params = [dependencies: value]

        when:
        SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message.contains "Unsupported dependency: '$value'. Supported: ${supportedOptions['dependencies'].join(', ')}."

        where:
        value << ['ai',
                  'akka',
                  'metabase']
    }

    @Unroll
    def 'buildQueryParams rejects incorrect groupId and packageName'() {
        given:
        Map<String, Object> params = [:]
        params[key] = value
        def prettyField = key.replaceAll(/([A-Z])/, ' $1').toLowerCase(Locale.ROOT)

        when:
        SpringInitializrParamsBuilder.buildQueryParams params, supportedOptions

        then:
        def ex = thrown InvalidUserDataException

        and:
        ex.message.contains "Invalid $prettyField: '$value'. Must be a valid Java package (e.g. com.example)."

        where:
        key           || value
        'groupId'     || 'with-dash'
        'packageName' || '.invalid'
    }
}
