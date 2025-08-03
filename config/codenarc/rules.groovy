ruleset {
    ruleset 'rulesets/basic.xml'
    ruleset 'rulesets/braces.xml'
    ruleset('rulesets/convention.xml') {
        CompileStatic {
            enabled = false
        }
        ImplicitReturnStatement {
            enabled = false
        }
        MethodReturnTypeRequired {
            doNotApplyToClassNames = '*Spec'
        }
        NoDef {
            doNotApplyToClassNames = '*Spec'
        }
        VariableTypeRequired {
            doNotApplyToClassNames = '*Spec'
        }
    }
    ruleset('rulesets/dry.xml') {
        DuplicateStringLiteral {
            doNotApplyToClassNames = 'BootVersionUtils,SpringInitializrParamsUtils,SpringInitializrParamsBuilder,InitSpringBootProjectTask'
        }
    }
    ruleset 'rulesets/groovyism.xml'
    ruleset 'rulesets/imports.xml'
    ruleset('rulesets/naming.xml') {
        FactoryMethodName {
            doNotApplyToClassNames = 'SpringInitializrParamsBuilderSpec'
        }
        MethodName {
            doNotApplyToClassNames = '*Spec'
        }
    }
    ruleset 'rulesets/unused.xml'
}
