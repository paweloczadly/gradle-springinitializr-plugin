package io.oczadly.internal.utils

import groovy.transform.CompileStatic

@CompileStatic
class UrlUtils {

    static String toQueryString(Map<String, String> params) {
        params.collect { k, v -> "${encodeParam(k)}=${encodeParam(v)}" }.join('&')
    }

    private static String encodeParam(String s) {
        URLEncoder.encode s ?: '', 'UTF-8'
    }
}
