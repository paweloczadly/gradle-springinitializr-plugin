package io.oczadly.internal.generator

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.oczadly.internal.config.PluginConfig
import io.oczadly.internal.config.PluginConstants
import io.oczadly.testsupport.MetadataServiceTestUtils
import org.gradle.api.logging.Logger
import spock.lang.Specification

class MetadataServiceFunctionalSpec extends Specification {

    Logger logger

    String metadataUrl

    WireMockServer wireMockServer

    def setup() {
        logger = Mock Logger
        String initializrVersion = PluginConfig.getOrThrow PluginConstants.SPRING_INITIALIZR_VERSION

        // Configure WireMock to load stubs from classpath
        wireMockServer = new WireMockServer(options()
                .port(0)
                .usingFilesUnderClasspath("META-INF/io.spring.initializr/initializr-web/$initializrVersion"))
        wireMockServer.start()

        // Add fallback stub for testing when original stubs don't match our headers
        // Use very low priority to ensure original stubs are tried first
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl())
                .atPriority(1000)  // Very low priority - only used when NO original stubs match
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader('Content-Type', 'application/vnd.initializr.v2.1+json;charset=UTF-8')
                        .withBody(MetadataServiceTestUtils.TEST_METADATA)))

        metadataUrl = "http://localhost:${wireMockServer.port()}"
    }

    def cleanup() {
        wireMockServer?.stop()
    }

    def 'should extract supported options from stub'() {
        given:
        def result = MetadataService.extractSupportedOptions metadataUrl, logger

        expect:
        result.type
        result.language
        result.bootVersion
        result.packaging
        result.javaVersion
        result.dependencies
    }

    def 'should extract defaults from stub'() {
        given:
        def result = MetadataService.extractDefaults metadataUrl, logger

        expect:
        result.type
        result.language
        result.bootVersion
        result.packaging
        result.javaVersion
        result.groupId
        result.artifactId
        result.name
        result.description
        result.packageName
    }
}
