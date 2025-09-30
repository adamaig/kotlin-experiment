// Domain module - pure Kotlin with no external dependencies
// Following constitutional requirement for domain purity

plugins {
    kotlin("jvm")
}

dependencies {
    // Only Kotlin stdlib - no external dependencies per constitution
    implementation(kotlin("stdlib"))
    
    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
