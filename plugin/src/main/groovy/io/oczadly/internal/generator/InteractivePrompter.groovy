package io.oczadly.internal.generator

import groovy.transform.CompileStatic

@CompileStatic
class InteractivePrompter {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))

    String ask(String prompt, String defaultValue = null) {
        print "\n$prompt (default: $defaultValue): "
        System.out.flush()
        String line = reader.readLine()?.trim()
        line ?: defaultValue
    }

    String askChoice(String prompt, List<String> options, int defaultIndex = 0) {
        println "\n$prompt"

        options.eachWithIndex { String opt, int i ->
            println " ${i + 1}: $opt"
        }

        String range = "[1..${options.size()}]"
        while (true) {
            print "Enter selection (default: ${options[defaultIndex]}) $range "
            System.out.flush()
            String input = reader.readLine()?.trim()

            if (!input) {
                return options[defaultIndex]
            }
            if (input.isInteger()) {
                Integer index = input.toInteger() - 1
                if (index >= 0 && index < options.size()) {
                    return options[index]
                }
            }

            print "Please enter a value between 1 and ${options.size()} "
            System.out.flush()
        }
    }
}
