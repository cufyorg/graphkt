const val kotlin_version = "1.7.22"
const val ktor_version = "2.0.2"

object Dependencies {
    object Kotlin {
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
        const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"
        const val coroutines_reactive = "org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.4"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:1.7.22"
    }

    object Ktor {
        const val server_core_jvm = "io.ktor:ktor-server-core-jvm:$ktor_version"
        const val server_websockets = "io.ktor:ktor-server-websockets:$ktor_version"

        const val serialization_kotlinx_json_jvm =
            "io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version"

        const val host_common_jvm = "io.ktor:ktor-server-host-common-jvm:$ktor_version"
        const val content_negotiation_jvm = "io.ktor:ktor-server-content-negotiation-jvm:$ktor_version"
        const val cors_jvm = "io.ktor:ktor-server-cors-jvm:$ktor_version"
        const val default_headers_jvm = "io.ktor:ktor-server-default-headers-jvm:$ktor_version"
        const val http_redirect_jvm = "io.ktor:ktor-server-http-redirect-jvm:$ktor_version"
        const val netty_jvm = "io.ktor:ktor-server-netty-jvm:$ktor_version"
        const val server_tests_jvm = "io.ktor:ktor-server-tests-jvm:$ktor_version"
        const val call_logging_jvm = "io.ktor:ktor-server-call-logging-jvm:$ktor_version"
    }

    object Logging {
        const val logback_core = "ch.qos.logback:logback-core:1.2.11"
        const val logback_classic = "ch.qos.logback:logback-classic:1.2.11"
        const val self4j = "org.slf4j:slf4j-api:1.7.36"
    }

    const val graphql_java = "com.graphql-java:graphql-java:18.1"
    const val rxjava = "io.reactivex.rxjava2:rxjava:2.1.5"
}
