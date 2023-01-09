import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version kotlin_version
    kotlin("plugin.serialization") version kotlin_version
    id("maven-publish")
}

group = "org.cufy"
version = "2.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ktor"))
    implementation(project(":graphql-java"))

    implementation(kotlin("stdlib"))

    implementation(Dependencies.Kotlin.serialization)
    implementation(Dependencies.Kotlin.coroutines_core)
    implementation(Dependencies.Kotlin.coroutines_reactive)
    implementation(Dependencies.Kotlin.reflect)

    testImplementation(Dependencies.Ktor.server_core_jvm)
    testImplementation(Dependencies.Ktor.host_common_jvm)
    testImplementation(Dependencies.Ktor.netty_jvm)
    testImplementation(Dependencies.Ktor.call_logging_jvm)
    testImplementation(Dependencies.Ktor.content_negotiation_jvm)
    testImplementation(Dependencies.Ktor.serialization_kotlinx_json_jvm)

    testImplementation(Dependencies.Logging.logback_core)
    testImplementation(Dependencies.Logging.logback_classic)
    testImplementation(Dependencies.Logging.self4j)

    testImplementation(Dependencies.rxjava)
    testImplementation(Dependencies.graphql_java)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                artifactId = "graphkt"
            }
        }
    }
}
