plugins {
    id("java")
    application
}

group = "org.wmn4j.ismir.demo"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.wmn4j:wmn4j:0.7.0")

    // Add logger implementation for wmn4j to use
    implementation("org.slf4j:slf4j-jdk14:2.0.3")
}

application {
    mainClass.set("org.wmn4j.ismir.demo.Main")
}


