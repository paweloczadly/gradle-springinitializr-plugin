package io.oczadly.internal.generator

import groovy.transform.CompileStatic
import io.oczadly.internal.utils.SpringInitializrParamsUtils

@CompileStatic
class SpringInitializrParamsBuilder {

    private static final List<String> ORDER = ['type', 'language', 'bootVersion',
                                               'groupId', 'artifactId', 'name', 'description', 'packageName',
                                               'packaging', 'javaVersion', 'dependencies']

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
                case 'packaging':
                case 'javaVersion':
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.allowedOption(key, r, validationLists)
                    }
                    break

                case 'bootVersion':
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.bootVersion(r, validationLists?.get('bootVersion'))
                    }
                    break

                case 'dependencies':
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.dependencies(r, validationLists?.get('dependencies'))
                    }
                    break

                case 'groupId':
                case 'packageName':
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.javaPackage(r, key)
                    }
                    break

                default:
                    putIfPresent(out, key, raw) { r ->
                        SpringInitializrParamsUtils.text(r)
                    }
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
