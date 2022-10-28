import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("maven-publish")
}

group = "org.cufy"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")

    implementation("io.ktor:ktor-server-core-jvm:2.0.2")

//    implementation("com.expediagroup:graphql-kotlin-server:6.0.0-alpha.4")
//    implementation("com.expediagroup:graphql-kotlin-federation:6.0.0-alpha.4")
//    implementation("com.expediagroup:graphql-kotlin-dataloader:6.0.0-alpha.4")
//    implementation("com.expediagroup:graphql-kotlin-schema-generator:6.0.0-alpha.4")

    implementation("com.graphql-java:graphql-java:18.1")
    implementation("com.google.guava:guava:31.1-jre")

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
                artifactId = "kaguya"
            }
        }
    }
}
