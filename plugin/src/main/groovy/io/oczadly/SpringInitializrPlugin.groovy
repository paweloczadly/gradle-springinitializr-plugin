package io.oczadly

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.tasks.InitSpringBootProjectTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider

class SpringInitializrPlugin implements Plugin<Project> {

    void apply(Project project) {
        String taskName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_NAME
        String groupName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_GROUP_NAME
        String description = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_DESCRIPTION

        ProviderFactory providers = project.providers

        Provider<String> projectTypeProp = providers.gradleProperty 'projectType'
        Provider<String> languageProp = providers.gradleProperty 'language'
        Provider<String> bootVersionProp = providers.gradleProperty 'bootVersion'
        Provider<String> groupIdProp = providers.gradleProperty 'groupId'
        Provider<String> artifactIdProp = providers.gradleProperty 'artifactId'
        Provider<String> projectNameProp = providers.gradleProperty 'projectName'
        Provider<String> projectDescriptionProp = providers.gradleProperty 'projectDescription'
        Provider<String> packageNameProp = providers.gradleProperty 'packageName'
        Provider<String> packagingProp = providers.gradleProperty 'packaging'
        Provider<String> javaVersionProp = providers.gradleProperty 'javaVersion'
        Provider<String> dependenciesProp = providers.gradleProperty 'dependencies'

        ProjectLayout layout = project.layout
        Provider<Directory> defaultOutputDir = layout.buildDirectory.dir(
                PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_OUTPUTDIR_DEFAULT
        )
        Provider<Directory> cliOutputDir = providers.gradleProperty('outputDir')
                .flatMap { path ->
                    providers.provider { layout.projectDirectory.dir path }
                }
        Provider<Directory> outputDirProp = cliOutputDir.orElse defaultOutputDir

        String initializrUrlDefault = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_INITIALIZRURL_DEFAULT
        String metadataEndpointDefault = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_METADATAENDPOINT_DEFAULT
        String extractDefault = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_EXTRACT_DEFAULT

        TaskProvider<InitSpringBootProjectTask> taskProvider = project.tasks.register taskName, InitSpringBootProjectTask
        taskProvider.configure { task ->
            task.group = groupName
            task.description = description

            // -P parameters:
            task.projectType.set projectTypeProp
            task.language.set languageProp
            task.bootVersion.set bootVersionProp
            task.groupId.set groupIdProp
            task.artifactId.set artifactIdProp
            task.projectName.set projectNameProp
            task.projectDescription.set projectDescriptionProp
            task.packageName.set packageNameProp
            task.packaging.set packagingProp
            task.javaVersion.set javaVersionProp
            task.dependencies.set dependenciesProp
            task.outputDir.set outputDirProp

            // convention:
            task.initializrUrl.convention initializrUrlDefault
            task.metadataEndpoint.convention metadataEndpointDefault
            task.extract.convention extractDefault
        }
    }
}
