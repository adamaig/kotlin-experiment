// HTTP API application module

plugins {
    kotlin("jvm")
    application
    kotlin("plugin.serialization") version "1.9.25"
}

val ktor_version: String by project

dependencies {
    // Module dependencies
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    
    // Ktor server dependencies
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    
    // Implementation dependencies
    implementation(kotlin("stdlib"))
    
    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}

application {
    mainClass.set("ServerKt")
}
