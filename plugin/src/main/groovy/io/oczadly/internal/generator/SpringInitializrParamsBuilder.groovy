package io.oczadly.internal.generator

import groovy.transform.CompileStatic
import io.oczadly.internal.utils.SpringInitializrParamsUtils

@CompileStatic
class SpringInitializrParamsBuilder {

    private static final List<String> ORDER = ['type', 'language']

    static LinkedHashMap<String, String> buildQueryParams(Map<String, Object> paramsMap, Map<String, List<String>> validationLists) {
        LinkedHashMap<String, String> out = [:]

        for (String key : ORDER) {
            Object raw = paramsMap[key]
            if (!SpringInitializrParamsUtils.hasText(raw)) {
                continue
            }

            switch (key) {
                case 'type':
                case 'language':
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.allowedOption key, r, validationLists
                    }
                    break
            }
        }

        out
    }

    private static void putIfPresent(Map<String, String> out, String key, Object raw, Closure<String> compute) {
        final String val = (String) compute.call(raw)
        if (SpringInitializrParamsUtils.hasText(val)) {
            out[key] = val
        }
    }
}
