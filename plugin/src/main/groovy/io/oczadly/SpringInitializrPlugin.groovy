package io.oczadly

import org.gradle.api.Plugin
import org.gradle.api.Project

class SpringInitializrPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.tasks.register('greeting') {
            doLast {
                println "Hello from plugin 'io.oczadly.springinitializr'"
            }
        }
    }
}
