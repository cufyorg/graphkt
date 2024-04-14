package testing

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import org.cufy.graphkt.java.`graphql-java`
import org.cufy.graphkt.ktor.graphql
import org.cufy.graphkt.ktor.graphqlPlayground
import org.cufy.graphkt.ktor.schema
import org.cufy.graphkt.schema.*
import org.slf4j.event.Level
import java.time.Duration

class CustomScalar(val value: String)
class CustomObject(val name: String, val value: String)

val CustomFlow = MutableSharedFlow<CustomObject>()

val CustomScalarType = GraphQLScalarType<CustomScalar> {
    name = "CustomScalar"
    description = "An example of a custom scalar."
    encode { GraphQLString(it.value) }
    decode {
        require(it is GraphQLString) { "Expected GraphQLString but got ${it::class.simpleName}" }
        CustomScalar(it.value)
    }
}

object CustomObjectType : GraphQLObjectClass<CustomObject>({
    name = "Custom"
    description = "An example of a custom object type."
    field(CustomObject::name) {
        description = "The name of the object."
        type { GraphQLStringType }
    }
    field(CustomObject::value) {
        description = "The value of the object."
        type { GraphQLStringType }
    }
    field("this") {
        description = "This"
        type { CustomObjectType }
        get { instance }
    }
    field("copycatOfThis") {
        description = "This but as the copycat version."
        type { CustomObjectTypeCopyCat }
        get { instance }
    }
})

object CustomObjectTypeCopyCat : GraphQLObjectClass<CustomObject>({
    name = "CustomCopyCat"
    description = "An example of a custom object type copying another type."

    directives += CustomObjectType.directives
    interfaces += CustomObjectType.interfaces
    onGetBlocks += CustomObjectType.onGetBlocks
    onGetBlockingBlocks += CustomObjectType.onGetBlockingBlocks
    fields += CustomObjectType.fields
})

fun main() {
    embeddedServer(
        Netty,
        4444,
        module = Application::myApplicationModule
    ).start(true)
}

fun Application.myApplicationModule() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    graphqlPlayground()
//    graphiql()
    graphql {
        `graphql-java`
        graphqls = "/graphqls"

        schema {
            query {
                queries()
            }
            mutation {
                mutations()
            }
            subscription {
                subscriptions()
            }
        }
    }
}

@GraphQLDsl
fun GraphQLRoute<Unit>.queries() {
    field("cat") {
        description = "Return the input"
        type { CustomScalarType }

        val valueArg = argument("value") {
            description = "The value to return"
            type { CustomScalarType }
        }

        get { valueArg() }
    }
}

@GraphQLDsl
fun GraphQLRoute<Unit>.mutations() {
    field("emit") {
        type { GraphQLVoidType }

        val nameArg = argument("name") {
            description = "The name to be associated with the emitted value."
            type { GraphQLStringType }
        }
        val valueArg = argument("value") {
            description = "The value to be emitted"
            type { GraphQLStringType }
        }

        get {
            val name = nameArg()
            val value = valueArg()

            CustomFlow.emit(CustomObject(name, value))
        }
    }
}

@GraphQLDsl
fun GraphQLRoute<Unit>.subscriptions() {
    field("collect") {
        type { CustomObjectType }

        val nameArg = argument("name") {
            description = "The name to filter the messages by."
            type { GraphQLStringType }
        }

        getFlow {
            val name = nameArg()

            CustomFlow.filter { it.name == name }
        }
    }
}
