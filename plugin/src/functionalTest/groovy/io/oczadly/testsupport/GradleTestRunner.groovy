package io.oczadly.testsupport

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

class GradleTestRunner {

    File projectDir
    File gradleUserHomeDir
    List<String> args = []
    boolean shouldFail = false

    static List<String> asListOfStrings(List args) {
        args.findAll().collect(String.&valueOf) as List<String>
    }

    BuildResult run() {
        List<String> baseArgs = gradleUserHomeDir ?
                asListOfStrings(["--gradle-user-home=${gradleUserHomeDir.absolutePath}"] + args) : args

        GradleRunner runner = GradleRunner.create()
                .withPluginClasspath()
                .withArguments(baseArgs)
                .withProjectDir(projectDir)
                .forwardOutput()

        shouldFail ? runner.buildAndFail() : runner.build()
    }
}
