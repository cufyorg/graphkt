plugins {
    kotlin("jvm") version kotlin_version
    kotlin("plugin.serialization") version kotlin_version
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":core"))

    implementation(kotlin("stdlib"))

    implementation(Dependencies.Kotlin.coroutines_core)
    implementation(Dependencies.Kotlin.coroutines_reactive)
    implementation(Dependencies.Kotlin.serialization)
    implementation(Dependencies.Kotlin.reflect)

    implementation(Dependencies.rxjava)
    implementation(Dependencies.graphql_java)

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
