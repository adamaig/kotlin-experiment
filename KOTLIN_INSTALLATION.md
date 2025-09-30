# Kotlin Development Environment

This repository contains a complete setup for Kotlin development in a dev container.

## 🚀 Quick Setup

Run the setup script to install all necessary tools:

```bash
./install-kotlin-and-java-tools.sh
```

This script will install:
- **Kotlin** (latest stable version)
- **Spring Boot CLI** (for Spring Boot projects)
- **Gradle** (build automation)
- **Maven** (build automation)
- **SDKMAN!** (SDK manager)

## 📋 What's Included

### Core Tools
- ✅ **Java 21** (OpenJDK, pre-installed)
- ✅ **Kotlin 2.2.20** (latest stable)
- ✅ **Kotlin Compiler** (kotlinc)
- ✅ **Gradle 9.1.0** (build tool)
- ✅ **Maven 3.9.11** (build tool)
- ✅ **Spring Boot CLI 3.5.6** (Spring Boot project generator)

### Development Tools
- ✅ **SDKMAN!** (SDK version management)
- ✅ **Git** (version control)
- ✅ **Node.js & npm** (for tooling)
- ✅ **Docker CLI** (containerization)

## 🏃‍♂️ Quick Start

### 1. Simple Kotlin Program
```bash
# Create a Kotlin file
cat > Hello.kt << 'EOF'
fun main() {
    println("Hello, Kotlin!")
}
EOF

# Compile and run
kotlinc Hello.kt -include-runtime -d Hello.jar
java -jar Hello.jar
```

### 2. Gradle Project
```bash
# Initialize a new Kotlin application
gradle init --type kotlin-application --project-name my-kotlin-app

# Build and run
cd my-kotlin-app
./gradlew run
```

### 3. Spring Boot Project
```bash
# Create a new Spring Boot Kotlin project
spring init --dependencies=web --language=kotlin --build=gradle my-spring-app

# Build and run
cd my-spring-app
./gradlew bootRun
```

## 🛠 Useful Commands

### Kotlin Commands
```bash
kotlin -version              # Check Kotlin version
kotlinc Hello.kt            # Compile Kotlin file
kotlinc -script hello.kts   # Run Kotlin script
```

### SDKMAN! Commands
```bash
sdk list kotlin             # List available Kotlin versions
sdk install kotlin 2.1.0   # Install specific version
sdk use kotlin 2.1.0       # Switch version for current session
sdk default kotlin 2.1.0   # Set default version
sdk current                 # Show current versions
sdk upgrade                 # Upgrade all installed SDKs
```

### Gradle Commands
```bash
gradle init                 # Initialize new project
gradle build               # Build project
gradle run                 # Run application
gradle test                # Run tests
gradle clean               # Clean build directory
```

### Maven Commands
```bash
mvn archetype:generate     # Create new project
mvn compile               # Compile source code
mvn test                  # Run tests
mvn package               # Create JAR/WAR
mvn clean                 # Clean target directory
```

## 📁 Project Structure

```
kotlin-experiment/
├── setup.sh              # Main setup script
├── install-sdks.sh       # Original SDK installation script
├── Hello.kt              # Sample Kotlin application
├── README.md             # This file
└── .devcontainer/        # Dev container configuration
```

## 🔧 Advanced Configuration

### IDE Extensions (Recommended)
- **Kotlin Language** - Kotlin syntax support
- **Gradle for Java** - Gradle build support
- **Spring Boot Extension Pack** - Spring Boot development
- **GitLens** - Git integration

### Environment Variables
The setup script automatically configures:
- `JAVA_HOME` - Java installation path
- `KOTLIN_HOME` - Kotlin installation path
- `GRADLE_HOME` - Gradle installation path
- `MAVEN_HOME` - Maven installation path

## 🐛 Troubleshooting

### Common Issues

1. **Command not found after installation**
   ```bash
   source ~/.bashrc
   # or
   source ~/.zshrc
   ```

2. **SDKMAN! not working**
   ```bash
   source /usr/local/sdkman/bin/sdkman-init.sh
   ```

3. **Permission denied on setup.sh**
   ```bash
   chmod +x setup.sh
   ```

### Getting Help
```bash
sdk help                   # SDKMAN! help
gradle help               # Gradle help
mvn help:help             # Maven help
```

## 📚 Learning Resources

- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Kotlin Koans](https://kotlinlang.org/docs/koans.html)
- [Spring Boot with Kotlin](https://spring.io/guides/tutorials/spring-boot-kotlin/)
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

## 🤝 Contributing

Feel free to improve this setup by:
1. Adding more development tools
2. Improving the setup script
3. Adding more examples
4. Updating documentation

---

Happy Kotlin coding! 🎉
