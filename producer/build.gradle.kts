plugins {
    kotlin("jvm") version "1.8.0"
    id("producer")
}

producer {
    features.set(listOf("three"))
    //features.set(emptyList())
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}