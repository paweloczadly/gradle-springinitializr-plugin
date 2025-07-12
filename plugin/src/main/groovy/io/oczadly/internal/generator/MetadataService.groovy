package io.oczadly.internal.generator

import groovy.json.JsonSlurper
import org.gradle.api.logging.Logger

class MetadataService {

    static Map<String, List<String>> extractSupportedOptions(String metadataUrl, Logger logger) {
        Object parsed = fetchMetadata metadataUrl, logger

        List<String> type = parsed?.type?.values*.id
        List<String> language = parsed?.language?.values*.id

        [type: type, language: language]
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
