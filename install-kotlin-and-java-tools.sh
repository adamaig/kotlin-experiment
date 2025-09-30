#!/usr/bin/env bash

# Kotlin Development Environment Setup Script
# This script sets up a complete Kotlin development environment in a dev container

set -e  # Exit on any error

echo "🚀 Starting Kotlin Development Environment Setup..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
print_status "Checking Java installation..."
if command -v java >/dev/null 2>&1; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    print_success "Java is installed: $JAVA_VERSION"
else
    print_error "Java is not installed. Please install Java first."
    exit 1
fi

# Install/Update SDKMAN!
print_status "Setting up SDKMAN!..."
if [ -d "/usr/local/sdkman" ] || [ -d "$HOME/.sdkman" ]; then
    print_warning "SDKMAN! already exists. Updating..."
    source /usr/local/sdkman/bin/sdkman-init.sh 2>/dev/null || source "$HOME/.sdkman/bin/sdkman-init.sh"
    sdk selfupdate force
else
    print_status "Installing SDKMAN!..."
    curl -s "https://get.sdkman.io" | bash
    source /usr/local/sdkman/bin/sdkman-init.sh 2>/dev/null || source "$HOME/.sdkman/bin/sdkman-init.sh"
fi

print_success "SDKMAN! is ready"

# Install Kotlin
print_status "Installing Kotlin..."
if sdk list kotlin | grep -q ">>> kotlin"; then
    print_warning "Kotlin is already installed"
    CURRENT_KOTLIN=$(sdk current kotlin | awk '{print $NF}')
    print_status "Current Kotlin version: $CURRENT_KOTLIN"
else
    sdk install kotlin
    print_success "Kotlin installed successfully"
fi

# Install Spring Boot CLI (useful for Kotlin Spring development)
print_status "Installing Spring Boot CLI..."
if sdk list springboot | grep -q ">>> springboot"; then
    print_warning "Spring Boot CLI is already installed"
    CURRENT_SPRINGBOOT=$(sdk current springboot | awk '{print $NF}')
    print_status "Current Spring Boot version: $CURRENT_SPRINGBOOT"
else
    sdk install springboot
    print_success "Spring Boot CLI installed successfully"
fi

# Install Gradle (popular build tool for Kotlin)
print_status "Installing Gradle..."
if sdk list gradle | grep -q ">>> gradle"; then
    print_warning "Gradle is already installed"
    CURRENT_GRADLE=$(sdk current gradle | awk '{print $NF}')
    print_status "Current Gradle version: $CURRENT_GRADLE"
else
    sdk install gradle
    print_success "Gradle installed successfully"
fi

# Install Maven (alternative build tool)
print_status "Installing Maven..."
if sdk list maven | grep -q ">>> maven"; then
    print_warning "Maven is already installed"
    CURRENT_MAVEN=$(sdk current maven | awk '{print $NF}')
    print_status "Current Maven version: $CURRENT_MAVEN"
else
    sdk install maven
    print_success "Maven installed successfully"
fi

# Verify installations
echo ""
print_status "Verifying installations..."

# Source SDKMAN! to ensure all tools are available
source /usr/local/sdkman/bin/sdkman-init.sh 2>/dev/null || source "$HOME/.sdkman/bin/sdkman-init.sh"

echo "📋 Installation Summary:"
echo "======================="

if command -v java >/dev/null 2>&1; then
    JAVA_VER=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo "✅ Java: $JAVA_VER"
else
    echo "❌ Java: Not found"
fi

if command -v kotlin >/dev/null 2>&1; then
    KOTLIN_VER=$(kotlin -version 2>&1 | awk '{print $3}')
    echo "✅ Kotlin: $KOTLIN_VER"
else
    echo "❌ Kotlin: Not found"
fi

if command -v kotlinc >/dev/null 2>&1; then
    KOTLINC_VER=$(kotlinc -version 2>&1 | awk '{print $2}')
    echo "✅ Kotlin Compiler: $KOTLINC_VER"
else
    echo "❌ Kotlin Compiler: Not found"
fi

if command -v gradle >/dev/null 2>&1; then
    GRADLE_VER=$(gradle -version 2>/dev/null | grep "Gradle" | head -1 | awk '{print $2}')
    echo "✅ Gradle: $GRADLE_VER"
else
    echo "❌ Gradle: Not found"
fi

if command -v mvn >/dev/null 2>&1; then
    MAVEN_VER=$(mvn -version 2>/dev/null | head -1 | awk '{print $3}')
    echo "✅ Maven: $MAVEN_VER"
else
    echo "❌ Maven: Not found"
fi

if command -v spring >/dev/null 2>&1; then
    SPRING_VER=$(spring --version 2>/dev/null | awk '{print $NF}')
    echo "✅ Spring Boot CLI: $SPRING_VER"
else
    echo "❌ Spring Boot CLI: Not found"
fi

echo ""
print_success "🎉 Kotlin development environment setup complete!"
echo ""
echo "💡 Quick Start Tips:"
echo "==================="
echo "• Create a Kotlin file: touch Hello.kt"
echo "• Compile Kotlin: kotlinc Hello.kt -include-runtime -d Hello.jar"
echo "• Run Kotlin JAR: java -jar Hello.jar"
echo "• Create Gradle project: gradle init --type kotlin-application"
echo "• Create Spring Boot project: spring init --dependencies=web --language=kotlin my-app"
echo ""
echo "📚 Useful Commands:"
echo "==================="
echo "• sdk list kotlin          - List available Kotlin versions"
echo "• sdk install kotlin X.Y.Z - Install specific Kotlin version"
echo "• sdk use kotlin X.Y.Z     - Switch to specific Kotlin version"
echo "• sdk current              - Show current versions of all SDKs"
echo ""
print_status "Happy Kotlin coding! 🚀"
