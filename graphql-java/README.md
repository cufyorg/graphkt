### GraphQL-Java Engine

This is a `graphkt` engine implementation for `graphql-java`

```kotlin
import org.cufy.graphkt.java.*
import org.cufy.graphkt.schema.*

fun main() {
    val schema = GraphQLSchema {
        // configure the schema
    }

    val engine = GraphQLJava(MySchema) {
        // configure the engine
    }

    // now it is possible to print the schema
    engine.printSchema(System.out)

    // or execute requests
    engine.execute(myRequest, myContext, myLocal)
}
```
