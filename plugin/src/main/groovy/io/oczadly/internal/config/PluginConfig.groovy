package io.oczadly.internal.config

import groovy.transform.CompileStatic

@CompileStatic
class PluginConfig {

    private static Properties config

    static String getOrThrow(String key) {
        String value = get().getProperty key
        if (value == null) {
            throw new IllegalStateException("Missing property: ${key} in plugin.properties")
        }
        value
    }

    static synchronized Properties get() {
        if (config == null) {
            config = new Properties()
            InputStream stream = PluginConfig.classLoader.getResourceAsStream 'plugin.properties'
            if (stream == null) {
                throw new IllegalStateException('Could not load plugin.properties from classpath')
            }
            config.load stream
        }
        config
    }
}
