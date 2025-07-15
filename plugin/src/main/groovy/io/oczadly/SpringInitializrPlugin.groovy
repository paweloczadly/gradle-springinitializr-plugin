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
            task.outputDir.set outputDirProp

            // convention:
            task.initializrUrl.convention initializrUrlDefault
            task.metadataEndpoint.convention metadataEndpointDefault
            task.extract.convention extractDefault
        }
    }
}
