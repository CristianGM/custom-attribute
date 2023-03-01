plugins {
    kotlin("jvm") version "1.8.0"
    id("consumer")
}

consumer {
    plugin(":producer")

    bundle {
        includedFeatures.set(listOf("one","two"))
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}