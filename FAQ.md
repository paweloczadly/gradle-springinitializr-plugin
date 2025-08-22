# FAQ

## Why another Spring Initializr wrapper?

`gradle-springinitializr-plugin` provides a **CI-friendly, build-cache-friendly way** to bootstrap Spring Boot projects using familiar Gradle workflows.

## Will this replace `curl` or the Spring Initializr UI?

It complements them, providing a **clean integration into your Gradle workflow**.

## Which Gradle versions are supported?

The supported versions can be found in the [compatibility-tests](.github/workflows/compatibility-tests.yaml#L21). Older versions are not guaranteed to work.

## Does it work with Kotlin DSL?

Yes. Please take a look at [examples/simple-kotlin](examples/simple-kotlin).

## Do I need Spring Boot CLI installed?

No. The plugin talks directly to the Spring Initializr API and does not depend on the CLI.

## Does it work offline?

A connection to the Spring Initializr server is required to generate a project. Dependencies resolved by Gradle can still be cached locally.

## Can I use it in commercial projects?

Yes. The plugin is released under the MIT license.

## Is interactive mode supported?

Yes, it is supported. However, right now it is experimental. If you want to use it, please make sure the following properties are added to your `gradle.properties`:

```properties
# Interactive mode:
org.gradle.console=plain
org.gradle.logging.level=quiet
```

And the following configuration exists in your `build.gradle` file:

```groovy
tasks.named('initSpringBootProject') {
    interactive = 'print'
}
```

Or please take a look at [examples/interactiveMode](examples/interactive-mode).

## Can I point to a custom Initializr server?

Yes. Please make sure the following configuration is added in your `build.gradle`:

```groovy
tasks.named('initSpringBootProject') {
    initializrUrl = 'https://myinitializr.mydomain.com'
    metadataEndpoint = '/mycustom/metadata'
}
```

Or please take a look at [examples/custom-initializr-server](examples/custom-initializr-server). This allows working with your own Initializr instances.

## Can I download a project from Spring Initializr only without extracting?

Yes. Please make sure the following configuration is added in your `build.gradle`:

```groovy
tasks.named('initSpringBootProject') {
    extract = 'false'
}
```

Or please take a look at [examples/download-only](examples/download-only).

## Can I force a specific Spring Boot version?

Yes. Please use `-PbootVersion=<version>`. If the version is not supported by Initializr, the plugin will fail.  

## How do I debug the plugin?

Please run Gradle with `--stacktrace --info`. You’ll see request/response logs from Initializr. 

## Can I request new features?

Yes! Please use the [feature request templates](https://github.com/paweloczadly/gradle-springinitializr-plugin/issues/new?template=10_feature_request.yaml) to request a new feature. Note, however, that:

> ⚠️ This plugin is developed and maintained in **focused time blocks** to ensure quality. Updates may be addressed on a **best-effort basis**.

## Can I report a bug?

Yes! Please use the [bug report templates](https://github.com/paweloczadly/gradle-springinitializr-plugin/issues/new?template=20_bug_report.yaml) to report a clear and reproducible issue.

> ⚠️ This plugin is developed and maintained in focused time blocks to ensure quality. Bug reports will be reviewed and addressed on a best-effort basis, depending on ongoing priorities.

## How do I contribute?

See [CONTRIBUTING.md](CONTRIBUTING.md) or open an issue to discuss your proposal.
