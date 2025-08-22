# Contributing to gradle-springinitializr-plugin

Thank you for considering contributing to **gradle-springinitializr-plugin**! Your improvements, ideas, and test cases are welcome.

## 🚀 Getting Started

1. **Fork this repository** and clone your fork.
2. Make sure you have:
* Java 17+
* Gradle (see supported versions in [CI matrix](.github/workflows/compatibility-tests.yaml#L21)).
* Internet access for fetching dependencies.
3. Run a full build to ensure a clean state:
    ```bash
    ./gradlew build
    ```

## ✍️ Making Changes

✅ Follow existing **code style and idioms** (Gradle, Groovy and Spock best practices).

✅ Ensure your change is:

* **Well-scoped** (one logical change per PR).
* Includes **unit or functional tests** in Spock.
* Passes **all checks** before submitting a PR.

✅ If adding a new feature:

Update `README.md` and `CHANGELOG.md`.

✅ If fixing a bug:

Include a test case demonstrating the bug and verifying the fix.

## 🧪 Testing locally

You can test your plugin against a test project by going to `examples/simple-groovy` or `examples/simple-kotlin` directory and execute from there:

```
$ gradle initSpringBootProject
```

## 🚦 Submitting a Pull Request

1. Push your changes to your fork.
2. Open a Pull Request. Please use the [pull request template](.github/pull_request_template.md) when opening a PR to ensure completeness.

## 🤝 Code of Conduct

Please be respectful and constructive in your communication. Contributions are welcome from all skill levels.

## ⚠️ Maintainer note

> This plugin is developed and maintained in **focused time blocks** to ensure high quality. Contributions and issues will be addressed on a **best-effort basis**, depending on ongoing priorities.

Thank you for helping make **gradle-springinitializr-plugin** better! 🚀
