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
}

application {
    mainClass.set("MainKt")
}
