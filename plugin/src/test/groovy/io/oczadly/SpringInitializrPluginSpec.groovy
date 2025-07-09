package io.oczadly

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SpringInitializrPluginSpec extends Specification {

    def 'plugin registers task'() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply 'io.oczadly.springinitializr'

        then:
        project.tasks.findByName('greeting') != null
    }
}
