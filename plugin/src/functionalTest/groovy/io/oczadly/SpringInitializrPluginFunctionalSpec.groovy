package io.oczadly

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

class SpringInitializrPluginFunctionalSpec extends Specification {

    @TempDir
    private File projectDir

    def 'can run task'() {
        given:
        settingsFile << ''
        buildFile << '''
plugins {
    id 'io.oczadly.springinitializr'
}
'''

        when:
        def result = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments('greeting')
                .withProjectDir(projectDir)
                .build()

        then:
        result.output.contains "Hello from plugin 'io.oczadly.springinitializr'"
    }

    private getBuildFile() {
        new File(projectDir, 'build.gradle')
    }

    private getSettingsFile() {
        new File(projectDir, 'settings.gradle')
    }
}
