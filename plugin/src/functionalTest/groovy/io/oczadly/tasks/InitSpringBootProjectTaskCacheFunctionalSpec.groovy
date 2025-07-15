package io.oczadly.tasks

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.testsupport.FilesTestUtils
import io.oczadly.testsupport.GradleTestRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir

class InitSpringBootProjectTaskCacheFunctionalSpec extends Specification {

    @TempDir
    private File testProjectDir

    @TempDir
    private File testCacheDir

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
        FilesTestUtils.projectFilesExist unzipDir, 'build.gradle', 'src/main/java/com/example/demo/DemoApplication.java'
    }

    def 'initSpringBootProject supports build cache'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def zipFile = new File(generatedProjectDir, zipName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                '-PprojectType=maven-project',
                '-Planguage=java',
                "-PoutputDir=${generatedProjectDir.absolutePath}",
                '--configuration-cache',
                '--build-cache',
        ])

        when: 'first run'
        def firstRun = new GradleTestRunner(projectDir: testProjectDir, gradleUserHomeDir: testCacheDir, args: args).run()

        then:
        firstRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        when: 'delete starter.zip'
        zipFile.delete()

        and: 'second run'
        def secondRun = new GradleTestRunner(projectDir: testProjectDir, gradleUserHomeDir: testCacheDir, args: args).run()

        then: 'should reuse cache'
        secondRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.FROM_CACHE
    }

    def 'initSpringBootProject supports incremental build'() {
        given:
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                '-PprojectType=gradle-project-kotlin',
                '-Planguage=kotlin',
                '--configuration-cache',
        ])

        when: 'first run'
        def firstRun = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        firstRun.output.contains 'Downloading Spring Boot starter project...'

        and:
        firstRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        when: 'second run with the same inputs'
        def secondRun = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        secondRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.UP_TO_DATE
    }

    def 'initSpringBootProject is not FROM_CACHE when inputs change'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def baseArgs = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
                '--configuration-cache',
                '--build-cache',
        ])
        def firstArgs = baseArgs
        def secondArgs = baseArgs + ['-PprojectType=gradle-project-kotlin', '-Planguage=kotlin']

        when: 'first run'
        def firstRun = new GradleTestRunner(projectDir: testProjectDir, gradleUserHomeDir: testCacheDir, args: firstArgs).run()

        then:
        firstRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        when: 'second run'
        def secondRun = new GradleTestRunner(projectDir: testProjectDir, gradleUserHomeDir: testCacheDir, args: secondArgs).run()

        then:
        secondRun.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS
    }
}
