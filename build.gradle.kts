plugins {
    kotlin("jvm") version kotlin_version apply false
    kotlin("plugin.serialization") version kotlin_version apply false
    id("maven-publish")
}

group = "org.cufy"
version = "2.0.0"

subprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    if (name == "example") return@subprojects

    group = "org.cufy.graphkt"

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])
                    artifactId = project.name
                }
            }
        }
    }
}
