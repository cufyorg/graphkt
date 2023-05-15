plugins {
    kotlin("jvm") version kotlin_version
    kotlin("plugin.serialization") version kotlin_version
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ktor"))
    implementation(project(":graphql-java"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("test"))

    implementation(Dependencies.Ktor.server_core_jvm)
    implementation(Dependencies.Ktor.server_websockets)
    implementation(Dependencies.Ktor.host_common_jvm)
    implementation(Dependencies.Ktor.netty_jvm)
    implementation(Dependencies.Ktor.call_logging_jvm)
    implementation(Dependencies.Ktor.content_negotiation_jvm)
    implementation(Dependencies.Ktor.serialization_kotlinx_json_jvm)

    implementation(Dependencies.Logging.logback_core)
    implementation(Dependencies.Logging.logback_classic)
    implementation(Dependencies.Logging.self4j)

    implementation(Dependencies.rxjava)
    implementation(Dependencies.graphql_java)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
