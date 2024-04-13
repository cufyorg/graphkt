plugins {
    `maven-publish`

    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":graphkt-core"))
                implementation(project(":graphkt-ktor"))
                implementation(project(":graphkt-graphql-java"))

                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.coroutines.reactive)

                implementation(libs.logback.core)
                implementation(libs.logback.classic)

                implementation(libs.self4j.api)

                implementation(libs.rxJava)
                implementation(libs.graphqlJava)

                implementation(libs.ktor.serialization.json)

                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.server.hostCommon)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.callLogging)
                implementation(libs.ktor.server.contentNegotiation)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
