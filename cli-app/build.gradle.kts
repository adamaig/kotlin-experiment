// CLI application module

plugins {
    kotlin("jvm")
    application
}

dependencies {
    // Module dependencies
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    
    // Implementation dependencies
    implementation(kotlin("stdlib"))
    
    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

application {
    mainClass.set("MainKt")
}
