package example

import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLNonNull
import io.ktor.server.application.*
import org.cufy.kaguya.GraphQLObjectType
import org.cufy.kaguya.ktor.graphql
import org.cufy.kaguya.reflection.field

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
