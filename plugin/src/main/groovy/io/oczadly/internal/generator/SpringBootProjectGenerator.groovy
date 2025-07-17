package io.oczadly.internal.generator

import groovy.transform.CompileStatic
import io.oczadly.internal.utils.UrlUtils
import io.oczadly.internal.utils.ZipUtils
import org.gradle.api.logging.Logger

@CompileStatic
class SpringBootProjectGenerator {

    private final Logger logger

    SpringBootProjectGenerator(Logger l) {
        logger = l
    }

    void generate(String initializrUrl, LinkedHashMap<String, String> queryParams, File outputZip, File unzipDir) {
        String query = UrlUtils.toQueryString queryParams
        URL url = new URI("$initializrUrl/starter.zip?$query").toURL()

        logger.debug "Downloading Spring Boot starter project from: $url"

        outputZip.parentFile.mkdirs()
        url.withInputStream { input ->
            outputZip.withOutputStream { output -> output << input }
        }

        logger.lifecycle "Project downloaded to: ${outputZip.absolutePath}"

        if (unzipDir) {
            ZipUtils.unzipToDir outputZip, unzipDir
            logger.lifecycle "Project extracted to: ${unzipDir?.absolutePath}"
            logger.debug "Files: ${unzipDir?.listFiles()}"
        }
    }
}
