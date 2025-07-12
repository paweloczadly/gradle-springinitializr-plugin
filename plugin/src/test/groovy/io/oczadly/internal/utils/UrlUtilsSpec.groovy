package io.oczadly.internal.utils

import spock.lang.Specification

class UrlUtilsSpec extends Specification {

    def 'toQueryString encodes parameters correctly'() {
        expect:
        UrlUtils.toQueryString([
                type    : 'gradle-project',
                language: 'java',
        ]) == 'type=gradle-project&language=java'
    }

    def 'toQueryString handles null and empty values'() {
        expect:
        UrlUtils.toQueryString([
                type    : null,
                language: '',
        ]) == 'type=&language='
    }

    def 'toQueryString encodes special characters'() {
        expect:
        UrlUtils.toQueryString([
                language: 'language?!@#$%^&*()_+',
        ]) == 'language=language%3F%21%40%23%24%25%5E%26*%28%29_%2B'
    }
}
