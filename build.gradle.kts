plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.litote.kmongo:kmongo-serialization:4.8.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
}