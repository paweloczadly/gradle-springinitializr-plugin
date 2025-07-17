package io.oczadly.tasks

import io.oczadly.internal.generator.MetadataService
import io.oczadly.internal.generator.SpringBootProjectGenerator
import io.oczadly.internal.utils.InputValidator
import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class InitSpringBootProjectTask extends DefaultTask {

    @Input
    abstract Property<String> getInitializrUrl()

    @Input
    abstract Property<String> getMetadataEndpoint()

    @Input
    abstract Property<String> getExtract()

    @Input
    @Optional
    abstract Property<String> getProjectType()

    @Input
    @Optional
    abstract Property<String> getLanguage()

    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @OutputFile
    File getOutputZipFile() {
        return new File(outputDir.get().asFile, PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_ZIPFILE_DEFAULT))
    }

    @OutputDirectory
    File getUnzipDir() {
        return new File(outputDir.get().asFile, PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTNAME_DEFAULT))
    }

    @TaskAction
    void download() {
        String metadataUrl = initializrUrl.get() + metadataEndpoint.get()
        Map<String, List<String>> metadata = MetadataService.extractSupportedOptions metadataUrl, logger
        List<String> supportedProjectTypes = metadata.type
        List<String> supportedLanguages = metadata.language

        String projectTypeValue = InputValidator.sanitize projectType.orNull, PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTTYPE_DEFAULT)
        String languageValue = InputValidator.sanitize language.orNull, PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_LANGUAGE_DEFAULT)

        InputValidator.validateSupportedValues projectTypeValue, supportedProjectTypes, 'project type'
        InputValidator.validateSupportedValues languageValue, supportedLanguages, 'language'

        LinkedHashMap<String, String> queryParams = ['type'    : projectTypeValue,
                                                     'language': languageValue,]

        SpringBootProjectGenerator generator = new SpringBootProjectGenerator(logger)
        generator.generate(initializrUrl.get(),
                queryParams,
                outputZipFile,
                extract.getOrNull()?.toBoolean() ? unzipDir : null)
    }
}
