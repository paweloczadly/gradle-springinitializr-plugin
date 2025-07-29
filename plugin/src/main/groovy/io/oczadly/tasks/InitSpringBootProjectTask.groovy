package io.oczadly.tasks

import io.oczadly.internal.generator.MetadataService
import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.internal.generator.SpringBootProjectGenerator
import io.oczadly.internal.generator.SpringInitializrParamsBuilder
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

    @Input
    @Optional
    abstract Property<String> getBootVersion()

    @Input
    @Optional
    abstract Property<String> getGroupId()

    @Input
    @Optional
    abstract Property<String> getArtifactId()

    @Input
    @Optional
    abstract Property<String> getProjectName()

    @Input
    @Optional
    abstract Property<String> getProjectDescription()

    @Input
    @Optional
    abstract Property<String> getPackageName()

    @Input
    @Optional
    abstract Property<String> getPackaging()

    @Input
    @Optional
    abstract Property<String> getJavaVersion()

    @Input
    @Optional
    abstract Property<String> getDependencies()

    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @OutputFile
    File getOutputZipFile() {
        return new File(outputDir.get().asFile, PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_ZIPFILE_DEFAULT))
    }

    @OutputDirectory
    File getUnzipDir() {
        return new File(outputDir.get().asFile, projectName.getOrElse(PluginConfig.getOrThrow(PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTNAME_DEFAULT)))
    }

    @TaskAction
    void download() {
        String metadataUrl = initializrUrl.get() + metadataEndpoint.get()
        Map<String, List<String>> supportedOptions = MetadataService.extractSupportedOptions metadataUrl, logger
        LinkedHashMap<String, String> queryParams = SpringInitializrParamsBuilder.buildQueryParams([type        : projectType.orNull,
                                                                                                    language    : language.orNull,
                                                                                                    bootVersion : bootVersion.orNull,
                                                                                                    groupId     : groupId.orNull,
                                                                                                    artifactId  : artifactId.orNull,
                                                                                                    name        : projectName.orNull,
                                                                                                    description : projectDescription.orNull,
                                                                                                    packageName : packageName.orNull,
                                                                                                    packaging   : packaging.orNull,
                                                                                                    javaVersion : javaVersion.orNull,
                                                                                                    dependencies: dependencies.orNull,], supportedOptions)

        SpringBootProjectGenerator generator = new SpringBootProjectGenerator(logger)
        generator.generate(initializrUrl.get(),
                queryParams,
                outputZipFile,
                extract.getOrNull()?.toBoolean() ? unzipDir : null)
    }
}
