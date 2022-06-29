# Kaguya Shinomiya [![](https://jitpack.io/v/org.cufy/kaguya.svg)](https://jitpack.io/#org.cufy/kaguya)

A GraphQL library based on
[graphql-java](http://github.com/graphql-java/graphql-java)
with kotlin-friendly builders and ktor routing extensions.

> Please, Don't touch my harem 😪

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
    implementation("org.cufy:kaguya:TAG")
}
```

### How to set up on ktor

The following is an example of using it.

```kotlin
data class Entity(
    val name: String
)

val EntityObjectType = GraphQLObjectType<Entity> {
    name = "Entity"
    description = "Some entity."

    field(Entity::name) {
        description = "The name of the entity."
    }

    field("nameWithCustomVar") {
        type = GraphQLNonNull(GraphQLString)

        resolver {
            it.name + graphQlContext.get("myCustomVar")
        }
    }
}

fun Application.configureGraphQL() {
    graphql {
        graphiql = true

        context {
            put("myCustomVar", Math.random())
        }

        schema {
            query {
                description = "The root query."

                field("getEntityWithName") {
                    type = EntityObjectType
                    description = "Get an entity instance."

                    val nameArg = argument<String> {
                        type = GraphQLString
                        name = "name"
                        description = "The name of the entity."
                    }

                    resolver {
                        Entity(nameArg())
                    }
                }
            }
        }
    }
}
```
