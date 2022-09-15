plugins {
    id("java")
}

group = "org.wmn4j.ismir.demo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.wmn4j:wmn4j:0.6.2")

    // Add logger implementation for wmn4j to use
    implementation("org.slf4j:slf4j-jdk14:1.7.36")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
