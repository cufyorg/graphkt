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
    val graphkt_version = "TAG"
    implementation("org.cufy.graphkt:graphkt-core:$graphkt_version")
    implementation("org.cufy.graphkt:graphkt-ktor:$graphkt_version")
    implementation("org.cufy.graphkt:graphkt-graphql-java:$graphkt_version")
}
```

### How to set up on ktor

The following is an example of using it.

```kotlin
data class Entity(
    val name: String
)

val EntityObjectType: GraphQLObjectType<Entity> = GraphQLObjectType {
    name = "Entity"
    description = "Some entity."

    field(Entity::name) {
        description = "The name of the entity."
        type { GraphQLStringType }
    }

    field("nameWithCustomVar") {
        description = "The name of the entity with the customVar in the context."
        type { GraphQLStringType.Nullable }

        get {
            it.name + context["myCustomVar"]
        }
    }
}

val EntitiesFlow = MutableSharedFlow<Entity>()

fun Application.configureGraphQL() {
    // you can choose any of these IDEs
    // graphiql()
    // apolloSandbox()
    graphqlPlayground() // recommended

    graphql {
        // add import org.cufy.graphkt.java.`graphql-java`
        `graphql-java` {
            // engine-specific configuration
        }

        before {
            context["myCustomVar"] = Math.random()
        }

        schema {
            query {
                description = "The root query."

                field("getEntityWithName") {
                    description = "Get an entity instance."
                    type { EntityObjectType }

                    val nameArg = argument<String>("name") {
                        description = "The name of the entity."
                        type { GraphQLStringType }
                    }

                    get { Entity(nameArg()) }
                }
            }
            mutation {
                description = "The root mutation"

                field("pushEntity") {
                    description = "Push an entity to subscribers"
                    type { EntityObjectType }

                    val nameArg = argument<String>("name") {
                        description = "The name of the entity."
                        type { GraphQLStringType }
                    }

                    get {
                        val entity = Entity(nameArg())
                        EntitiesFlow.emit(entity)
                        entity
                    }
                }
            }
            subscription {
                description = "The root subscription"

                field("subscribeToEntities") {
                    description = "Subscribe to pushed entities"
                    type { EntityObjectType }

                    getFlow { EntitiesFlow }
                }
            }
        }
    }
}
```
