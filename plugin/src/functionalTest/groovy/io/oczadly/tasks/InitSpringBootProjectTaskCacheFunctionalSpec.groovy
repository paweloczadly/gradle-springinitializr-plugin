package io.oczadly.tasks

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.testsupport.GradleTestRunner
import io.oczadly.testsupport.ZipTestUtils
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir

class InitSpringBootProjectTaskCacheFunctionalSpec extends Specification {

    @TempDir
    private File testProjectDir

    private final String initSpringBootProjectTaskName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_NAME
    private final String outputDir = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_OUTPUTDIR_DEFAULT
    private final String zipName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_ZIPFILE_DEFAULT
    private final String unzipDirName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTNAME_DEFAULT

    def setup() {
        def settingsFile = new File(testProjectDir, 'settings.gradle')
        settingsFile.text = ''

        def buildFile = new File(testProjectDir, 'build.gradle')
        buildFile.text = """
plugins {
    id '${PluginConfig.getOrThrow PluginConstants.PLUGIN_ID}'
}
"""
    }

    def 'initSpringBootProject supports configuration cache'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def zipFile = new File(generatedProjectDir, zipName)
        def unzipDir = new File(generatedProjectDir, unzipDirName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
                '--configuration-cache',
        ])

        when:
        def result = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        result.output.contains 'Downloading Spring Boot starter project...'

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        ZipTestUtils.zipFileExistsAndNotEmpty zipFile

        when:
        ZipTestUtils.unzipToDir zipFile, unzipDir

        then:
        ZipTestUtils.projectFilesExist unzipDir, 'build.gradle', 'src/main/java/com/example/demo/DemoApplication.java'
    }
}
