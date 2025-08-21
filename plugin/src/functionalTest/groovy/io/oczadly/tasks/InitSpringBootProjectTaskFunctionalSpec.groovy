package io.oczadly.tasks

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.internal.utils.BootVersionUtils
import io.oczadly.testsupport.FilesTestUtils
import io.oczadly.testsupport.GradleTestRunner
import io.oczadly.testsupport.MetadataServiceTestUtils
import org.gradle.api.logging.Logger
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
    private final String metadataUrl = PluginConfig.getOrThrow(PluginConstants.getTASK_INITSPRINGBOOTPROJECT_CONVENTION_INITIALIZRURL_DEFAULT()) +
            PluginConfig.getOrThrow(PluginConstants.getTASK_INITSPRINGBOOTPROJECT_CONVENTION_METADATAENDPOINT_DEFAULT())

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
        def unzipDir = new File(generatedProjectDir, unzipDirName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
        ])

        when:
        def result = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        result.output.contains "Project downloaded to: ${generatedProjectDir.absolutePath}"

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        FilesTestUtils.projectFilesExist unzipDir, 'build.gradle', 'src/main/java/com/example/demo/DemoApplication.java'
    }

    @Unroll
    def 'initSpringBootProject downloads project with type=#projectType and language=#language'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
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
        result.output.contains "Project downloaded to: ${generatedProjectDir.absolutePath}"

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        FilesTestUtils.projectFilesExist unzipDir, expectedBuildFile, expectedSourceFile

        where:
        projectType             | language | expectedBuildFile  | expectedSourceFile
        'maven-project'         | 'kotlin' | 'pom.xml'          | 'src/main/kotlin/com/example/demo/DemoApplication.kt'
        'gradle-project-kotlin' | 'kotlin' | 'build.gradle.kts' | 'src/main/kotlin/com/example/demo/DemoApplication.kt'
        null                    | null     | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
        'null'                  | 'null'   | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
        ''                      | ''       | 'build.gradle'     | 'src/main/java/com/example/demo/DemoApplication.java'
    }

    def 'initSpringBootProject uses correct groupId, artifactId, projectName, projectDescription, packageName, packaging, javaVersion and dependencies'() {
        given:
        def additionalDependencies = 'activemq, amqp'
        def expectedDependencies = additionalDependencies.split(', ').collect { dep -> "implementation 'org.springframework.boot:spring-boot-starter-$dep'" }
        def generatedProjectDir = new File(testProjectDir, unzipDirName)
        def projectName = 'spring-initializr'
        def unzipDir = new File(generatedProjectDir, projectName)
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                '-PgroupId=io.oczadly',
                '-PartifactId=spring-initializr',
                "-PprojectName=$projectName",
                '-PprojectDescription=Spring Initializr Project',
                '-PpackageName=io.oczadly.spring.initializr',
                '-Ppackaging=war',
                '-PjavaVersion=17',
                "-Pdependencies=$additionalDependencies",
                "-PoutputDir=${generatedProjectDir.absolutePath}",
        ])

        when:
        def result = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        result.output.contains "Project downloaded to: ${generatedProjectDir.absolutePath}"

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        FilesTestUtils.projectFilesExist unzipDir,
                'src/main/java/io/oczadly/spring/initializr/ServletInitializer.java',
                'src/main/java/io/oczadly/spring/initializr/SpringInitializrApplication.java',
                'src/main/resources/static',
                'src/main/resources/templates'

        and:
        FilesTestUtils.projectFilesContainText unzipDir, 'build.gradle', expectedDependencies
    }

    def 'initSpringBootProject does not extract when extract=false'() {
        given:
        def buildFile = new File(testProjectDir, 'build.gradle')
        buildFile.text = """
plugins {
    id '${PluginConfig.getOrThrow PluginConstants.PLUGIN_ID}'
}

tasks.named("$initSpringBootProjectTaskName") {
    extract.set 'false'
}
"""
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
        result.output.contains "Project downloaded to: ${generatedProjectDir.absolutePath}"

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        FilesTestUtils.zipFileExistsAndNotEmpty zipFile
        FilesTestUtils.unzipDirectoryDoesNotExist unzipDir
    }

    def 'initSpringBootProject is able to download project with random supported bootVersion'() {
        given:
        def generatedProjectDir = new File(testProjectDir, outputDir)
        def unzipDir = new File(generatedProjectDir, unzipDirName)
        def bootVersion = MetadataServiceTestUtils.getRandomBootVersion(metadataUrl, Mock(Logger))
        def args = GradleTestRunner.asListOfStrings([
                initSpringBootProjectTaskName,
                "-PoutputDir=${generatedProjectDir.absolutePath}",
                "-PbootVersion=$bootVersion",
        ])

        when:
        def result = new GradleTestRunner(projectDir: testProjectDir, args: args).run()

        then:
        result.output.contains "Project downloaded to: ${generatedProjectDir.absolutePath}"

        and:
        result.task(":$initSpringBootProjectTaskName").outcome == TaskOutcome.SUCCESS

        and:
        FilesTestUtils.projectFilesContainText unzipDir, 'build.gradle', BootVersionUtils.toRequestBootVersion(bootVersion)
    }
}
