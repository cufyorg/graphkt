# Graphkt [![](https://jitpack.io/v/org.cufy/graphkt.svg)](https://jitpack.io/#org.cufy/graphkt)

A GraphQL library based on
[graphql-java](http://github.com/graphql-java/graphql-java)
with kotlin-friendly builders and ktor routing extensions.

### Install

The main way of installing this library is
using `jitpack.io`

```kts
repositories {
    // ...
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Replace TAG with the desired version
    implementation("org.cufy:graphkt:TAG")
}
```

### How to set up on ktor

The following is an example of using it.

```kotlin
data class Entity(
    val name: String
)

val EntityObjectType = GraphQLObjectType<Entity>("Entity") {
    description { "Some entity." }

    field(Entity::name, GraphQLStringType) {
        description { "The name of the entity." }
    }

    field("nameWithCustomVar", GraphQLNullableType(GraphQLStringType)) {
        description { "The name of the entity with the customVar in the context." }

        get {
            it.name + context["myCustomVar"]
        }
    }
}

val EntitiesFlow = MutableSharedFlow<Entity>()

fun Application.configureGraphQL() {
    // you can choose any of these IDEs
    // graphiql()
    // sandbox()
    playground() // recommended

    graphql {
        // add import org.cufy.graphkt.java.`graphql-java`
        `graphql-java` {
            // engine-specific configuration
        }

        context {
            it["myCustomVar"] = Math.random()
        }

        schema {
            query {
                description { "The root query." }

                field("getEntityWithName", EntityObjectType) {
                    description { "Get an entity instance." }

                    val nameArg = argument<String>("name", GraphQLStringType) {
                        description { "The name of the entity." }
                    }

                    get { Entity(nameArg()) }
                }
            }
            mutation {
                description { "The root mutation" }

                field("pushEntity", EntityObjectType) {
                    description { "Push an entity to subscribers" }

                    val nameArg = argument<String>("name", GraphQLStringType) {
                        description { "The name of the entity." }
                    }

                    get {
                        val entity = Entity(nameArg())
                        EntitiesFlow.emit(entity)
                        entity
                    }
                }
            }
            subscription {
                description { "The root subscription" }

                field("subscribeToEntities", EntityObjectType) {
                    description { "Subscribe to pushed entities" }

                    getFlow { EntitiesFlow }
                }
            }
        }
    }
}
```
