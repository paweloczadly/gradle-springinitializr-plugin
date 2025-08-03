package io.oczadly

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SpringInitializrPluginSpec extends Specification {

    Project project

    Task task

    private final String initSpringBootProjectTaskName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_NAME
    private final String initializrUrl = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_INITIALIZRURL_DEFAULT
    private final String metadataEndpoint = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_METADATAENDPOINT_DEFAULT
    private final String extract = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_EXTRACT_DEFAULT
    private final String interactive = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_INTERACTIVE_DEFAULT

    private final String customInitializrUrl = 'https://start.spring.oczadly.io'
    private final String customMetadataEndpoint = '/metadata/spring'
    private final String customExtract = 'false'
    private final String customInteractive = 'print'

    def setup() {
        project = ProjectBuilder.builder().build()
        project.plugins.apply PluginConfig.getOrThrow(PluginConstants.PLUGIN_ID)

        task = project.tasks.initSpringBootProject
    }

    def 'plugin registers task'() {
        expect:
        project.tasks.findByName(initSpringBootProjectTaskName) != null
    }

    def 'initSpringBootProject task uses default initializr URL if not provided'() {
        expect:
        task.initializrUrl.get() == initializrUrl
    }

    def 'initSpringBootProject task uses custom initializrUrl from property'() {
        given:
        project.extensions.extraProperties.set 'initializrUrl', customInitializrUrl
        task.initializrUrl.set project.property('initializrUrl')

        expect:
        task.initializrUrl.get() == customInitializrUrl
    }

    def 'initSpringBootProject task uses default metadata endpoint if not provided'() {
        expect:
        task.metadataEndpoint.get() == metadataEndpoint
    }

    def 'initSpringBootProject task uses custom metadataEndpoint from property'() {
        given:
        project.extensions.extraProperties.set 'metadataEndpoint', customMetadataEndpoint
        task.metadataEndpoint.set project.property('metadataEndpoint')

        expect:
        task.metadataEndpoint.get() == customMetadataEndpoint
    }

    def 'initSpringBootProject task uses default extract if not provided'() {
        expect:
        task.extract.get() == extract
    }

    def 'initSpringBootProject task uses custom extract from property'() {
        given:
        project.extensions.extraProperties.set 'extract', customExtract
        task.extract.set project.property('extract')

        expect:
        task.extract.get() == customExtract
    }

    def 'initSpringBootProject task uses default interactive if not provided'() {
        expect:
        task.interactive.get() == interactive
    }

    def 'initSpringBootProject task uses custom interactive from property'() {
        given:
        project.extensions.extraProperties.set 'interactive', customInteractive
        task.extract.set project.property('interactive')

        expect:
        task.extract.get() == customInteractive
    }
}
