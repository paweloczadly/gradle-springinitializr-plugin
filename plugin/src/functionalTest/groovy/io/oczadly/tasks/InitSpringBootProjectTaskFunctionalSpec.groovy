package io.oczadly.tasks

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.testsupport.GradleTestRunner
import io.oczadly.testsupport.ZipTestUtils
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

class InitSpringBootProjectTaskFunctionalSpec extends Specification {

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

    def 'initSpringBootProject uses default parameters when not specified'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def zipFile = new File(generatedProjectDir, zipName)
        def unzipDir = new File(generatedProjectDir, unzipDirName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
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

    @Unroll
    def 'initSpringBootProject downloads project with type=#projectType and language=#language'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def zipFile = new File(generatedProjectDir, zipName)
        def unzipDir = new File(generatedProjectDir, unzipDirName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
                projectType ? "-PprojectType=$projectType" : null,
                projectType ? "-Planguage=$language" : null,
        ])

        when:
        def result = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        result.output.contains 'Downloading Spring Boot starter project'

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        ZipTestUtils.zipFileExistsAndNotEmpty zipFile

        when:
        ZipTestUtils.unzipToDir zipFile, unzipDir

        then:
        ZipTestUtils.projectFilesExist unzipDir, expectedBuildFile, expectedSourceFile

        where:
        projectType             | language | expectedBuildFile  | expectedSourceFile
        'maven-project'         | 'kotlin' | 'pom.xml'          | 'src/main/kotlin/com/example/demo/DemoApplication.kt'
        'gradle-project-kotlin' | 'kotlin' | 'build.gradle.kts' | 'src/main/kotlin/com/example/demo/DemoApplication.kt'
        null                    | null     | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
        'null'                  | 'null'   | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
        ''                      | ''       | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
    }
}
