# gradle-springinitializr-plugin

A **production-grade Gradle plugin** for **bootstrapping Spring Boot projects locally** using the [Spring Initializr API](https://start.spring.io), supporting **project metadata, build cache, and CI-friendly workflows**.

## 🚀 Features

- `projectType`, `language`, `bootVersion`, `groupId`, `artifactId`, `projectName`, `projectDescription`, `packageName`, `javaVersion`, `packaging`, `dependencies` with validation.

- interactive mode is supported, but experimental at this moment.

## ⚡ Usage

Inspired by the interactive UX of `gradle init`, this task walks you through Spring Initializr options — directly in your terminal.

<img src="docs/images/demo.gif" alt="Demo of gradle-springinitializr-plugin" width="75%" height="75%" />

See [FAQ.md](FAQ.md#is-interactive-mode-supported) for more information.

### ⚙️ Advanced usage with project metadata

You can customize project metadata with the following parameters:

- `groupId`
- `artifactId`
- `projectName`
- `projectDescription`
- `packageName`
- `packaging`
- `javaVersion`
- `dependencies`

Example:

```bash
$ gradle initSpringBootProject \
  -PprojectType=gradle-project-kotlin \
  -Planguage=kotlin \
  -PbootVersion=4.0.0-SNAPSHOT \
  -PoutputDir=my-spring-app \
  -PgroupId=com.mycompany \
  -PartifactId=my-spring-app \
  -PprojectName="My Spring App" \
  -PprojectDescription="My Spring Boot application generated via gradle-springinitializr-plugin" \
  -PpackageName=com.mycompany.myspringapp \
  -Ppackaging="war" \
  -PjavaVersion="21"
```

### 📈 Build Scan

Run with:

```bash
$ gradle initSpringBootProject --scan
```

to get a detailed build scan of your environment.

## 🤝 Contributing

Contributions are welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

> ⚠️ Please note that this plugin is developed and maintained in focused time blocks to ensure quality. Contributions and issues will be addressed on a best-effort basis, depending on ongoing priorities.

## 📄 License

MIT License – see [LICENSE](LICENSE) for details.

## 🙋 FAQ

See [FAQ.md](FAQ.md) for answers to common questions.
