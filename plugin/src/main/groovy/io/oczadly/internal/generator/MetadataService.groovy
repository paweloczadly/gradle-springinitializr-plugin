package io.oczadly.internal.generator

import groovy.json.JsonSlurper
import org.gradle.api.logging.Logger

class MetadataService {

    static Map<String, List<String>> extractSupportedOptions(String metadataUrl, Logger logger) {
        Object parsed = fetchMetadata metadataUrl, logger

        List<String> type = parsed?.type?.values*.id
        List<String> language = parsed?.language?.values*.id
        List<String> bootVersion = parsed?.bootVersion?.values*.id
        List<String> packaging = parsed?.packaging?.values*.id
        List<String> javaVersion = parsed?.javaVersion?.values*.id
        List<String> dependencies = parsed?.dependencies?.values*.values*.id

        [type: type, language: language, bootVersion: bootVersion, packaging: packaging, javaVersion: javaVersion, dependencies: dependencies?.flatten()]
    }

    static Map<String, Object> extractDefaults(String metadataUrl, Logger logger) {
        Object parsed = fetchMetadata metadataUrl, logger

        [type       : parsed?.type?.default,
         language   : parsed?.language?.default,
         bootVersion: parsed?.bootVersion?.default,
         packaging  : parsed?.packaging?.default,
         javaVersion: parsed?.javaVersion?.default,
         groupId    : parsed?.groupId?.default,
         artifactId : parsed?.artifactId?.default,
         name       : parsed?.name?.default,
         description: parsed?.description?.default,
         packageName: parsed?.packageName?.default,].findAll { item -> item.value }
    }

    private static Object fetchMetadata(String metadataUrl, Logger logger) {
        try {
            URL url = new URI(metadataUrl).toURL()
            new JsonSlurper().parse url
        } catch (Exception e) {
            logger.warn "Could not fetch metadata from $metadataUrl: ${e.message}"
            null
        }
    }
}
