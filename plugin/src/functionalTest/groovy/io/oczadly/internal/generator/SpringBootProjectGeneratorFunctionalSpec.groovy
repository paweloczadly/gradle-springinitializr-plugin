package io.oczadly.internal.generator

import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.testsupport.FilesTestUtils
import org.gradle.api.logging.Logger
import spock.lang.Specification
import spock.lang.TempDir

class SpringBootProjectGeneratorFunctionalSpec extends Specification {

    @TempDir
    private File testProjectDir

    private File outputZip

    private File unzipDir

    private SpringBootProjectGenerator generator

    private final String initializrUrl = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_CONVENTION_INITIALIZRURL_DEFAULT
    private final String zipName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_ZIPFILE_DEFAULT
    private final String unzipDirName = PluginConfig.getOrThrow PluginConstants.TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTNAME_DEFAULT

    def setup() {
        Logger logger = Mock Logger

        outputZip = new File(testProjectDir, zipName)
        unzipDir = new File(testProjectDir, unzipDirName)
        generator = new SpringBootProjectGenerator(logger)
    }

    def 'generate is able to download and extract project with empty map'() {
        given:
        LinkedHashMap<String, String> queryParams = [:]

        when:
        generator.generate initializrUrl, queryParams, outputZip, unzipDir

        then:
        FilesTestUtils.zipFileExistsAndNotEmpty outputZip
        FilesTestUtils.projectFilesExist unzipDir, 'build.gradle'
    }

    def 'generate is able to download and extract project with parameters'() {
        given:
        LinkedHashMap<String, String> queryParams = [type: 'gradle-project-kotlin', language: 'kotlin']

        when:
        generator.generate initializrUrl, queryParams, outputZip, unzipDir

        then:
        FilesTestUtils.zipFileExistsAndNotEmpty outputZip
        FilesTestUtils.projectFilesExist unzipDir, 'build.gradle.kts'
        FilesTestUtils.projectFilesExist unzipDir, 'src/main/kotlin/com/example/demo/DemoApplication.kt'
    }

    def 'generate should only download project without extracting'() {
        given:
        LinkedHashMap<String, String> queryParams = [:]

        when:
        generator.generate initializrUrl, queryParams, outputZip, null

        then:
        FilesTestUtils.zipFileExistsAndNotEmpty outputZip
        FilesTestUtils.unzipDirectoryDoesNotExist unzipDir
    }
}
