package io.oczadly.internal.config

import spock.lang.Specification
import spock.lang.Unroll

class PluginConfigSpec extends Specification {

    @Unroll
    def 'PluginConfig.getOrThrow() returns for key=#key'() {
        when:
        def value = PluginConfig.getOrThrow key

        then:
        value != null

        where:
        key << [
                'plugin.id',
                'task.initSpringBootProject.name',
                'task.initSpringBootProject.convention.initializrUrl.default',
        ]
    }

    @Unroll
    def 'PluginConfig.getOrThrow() throws exception for missing key=#key'() {
        when:
        PluginConfig.getOrThrow key

        then:
        thrown IllegalStateException

        where:
        key << [
                'non.existing.key',
                'task.initSpringBootProject.property.nonExistingProperty.fallback',
        ]
    }
}
