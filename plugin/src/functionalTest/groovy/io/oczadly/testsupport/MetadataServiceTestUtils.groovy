package io.oczadly.testsupport

import io.oczadly.internal.generator.MetadataService
import org.gradle.api.logging.Logger

class MetadataServiceTestUtils {

    static final String TEST_METADATA = '''{
            "type": {"type": "action", "default": "gradle-project", "values": [{"id": "gradle-project", "name": "Gradle Project"}]},
            "language": {"type": "single-select", "default": "java", "values": [{"id": "java", "name": "Java"}]},
            "bootVersion": {"type": "single-select", "default": "3.4.7", "values": [{"id": "3.4.7", "name": "3.4.7"}]},
            "packaging": {"type": "single-select", "default": "jar", "values": [{"id": "jar", "name": "Jar"}]},
            "javaVersion": {"type": "single-select", "default": "17", "values": [{"id": "17", "name": "17"}]},
            "dependencies": {"type": "hierarchical-multi-select", "values": [{
                "name": "Web",
                "values": [
                  {
                    "id": "web",
                    "name": "Spring Web",
                    "description": "Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container."
                  }
                ]
              }]
            },
            "groupId": {"type": "text", "default": "com.example"},
            "artifactId": {"type": "text", "default": "demo"},
            "name": {"type": "text", "default": "demo"},
            "description": {"type": "text", "default": "Demo project for Spring Boot"},
            "packageName": {"type": "text", "default": "com.example.demo"}
        }'''

    static String getRandomBootVersion(String metadataUrl, Logger logger) {
        Map<String, List<String>> metadata = MetadataService.extractSupportedOptions metadataUrl, logger
        List<String> bootVersions = (metadata?.bootVersion ?: []).findAll() as List<String>
        if (bootVersions.isEmpty()) {
            throw new IllegalStateException("No boot versions in metadata at $metadataUrl")
        }
        // Prefer RELEASE
        String release = bootVersions.find { v -> v.endsWith('.RELEASE') || !v.matches(/.*(-M\d+|-SNAPSHOT|BUILD-SNAPSHOT)/) }
        release ?: bootVersions.first()
    }
}
