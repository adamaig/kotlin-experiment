// Infrastructure module - depends on domain module

plugins {
    kotlin("jvm")
}

dependencies {
    // Domain dependency
    implementation(project(":domain"))
    
    // Implementation dependencies
    implementation(kotlin("stdlib"))
    
    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation(project(":domain"))
}

tasks.test {
    useJUnitPlatform()
}
