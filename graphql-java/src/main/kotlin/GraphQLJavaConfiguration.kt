/*
 *	Copyright 2022 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.graphkt.java

import graphql.ExecutionInput
import graphql.GraphQL
import graphql.schema.GraphQLSchema

/* ============================================== */
/* ========|                            |======== */
/* ========| Configuration Blocks       |======== */
/* ========|                            |======== */
/* ============================================== */

typealias GraphQLJavaSchemaTransformer =
        GraphQLSchema.Builder.() -> Unit

typealias GraphQLJavaInstanceTransformer =
        GraphQL.Builder.() -> Unit

typealias GraphQLJavaExecutionInputTransformer =
        ExecutionInput.Builder.() -> Unit

/* ============================================== */
/* ========|                            |======== */
/* ========| Java Configuration         |======== */
/* ========|                            |======== */
/* ============================================== */

/**
 * Configuration of graphql-java engine.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLJavaConfiguration {
    /**
     * Code to be executed to transform graphql schema.
     */
    val schemaTransformers: List<GraphQLJavaSchemaTransformer>

    /**
     * Code to be executed to transform graphql instance.
     */
    val graphqlTransformers: List<GraphQLJavaInstanceTransformer>

    /**
     * Code to be executed to transform execution input.
     */
    val executionInputTransformers: List<GraphQLJavaExecutionInputTransformer>
}

/**
 * A mutable variant of [GraphQLJavaConfiguration].
 *
 * @author LSafer
 * @since 2.0.0
 */
interface GraphQLJavaMutableConfiguration
    : GraphQLJavaConfiguration {
    override val schemaTransformers: MutableList<GraphQLJavaSchemaTransformer>
    override val graphqlTransformers: MutableList<GraphQLJavaInstanceTransformer>
    override val executionInputTransformers: MutableList<GraphQLJavaExecutionInputTransformer>
}

/**
 * Construct a new [GraphQLJavaConfiguration] with the given arguments.
 */
fun GraphQLJavaConfiguration(
    schemaTransformers: List<GraphQLJavaSchemaTransformer>,
    graphqlTransformers: List<GraphQLJavaInstanceTransformer>,
    executionInputTransformers: List<GraphQLJavaExecutionInputTransformer>
): GraphQLJavaConfiguration {
    return object : GraphQLJavaConfiguration {
        override val schemaTransformers = schemaTransformers
        override val graphqlTransformers = graphqlTransformers
        override val executionInputTransformers = executionInputTransformers
    }
}

/**
 * Construct a new [GraphQLJavaMutableConfiguration].
 */
fun GraphQLJavaMutableConfiguration(): GraphQLJavaMutableConfiguration {
    return object : GraphQLJavaMutableConfiguration {
        override val schemaTransformers = mutableListOf<GraphQLJavaSchemaTransformer>()
        override val graphqlTransformers = mutableListOf<GraphQLJavaInstanceTransformer>()
        override val executionInputTransformers = mutableListOf<GraphQLJavaExecutionInputTransformer>()
    }
}

/**
 * Obtain a copy of this with the given arguments.
 */
fun GraphQLJavaConfiguration.copy(
    schemaTransformers: List<GraphQLJavaSchemaTransformer> = this.schemaTransformers,
    graphqlTransformers: List<GraphQLJavaInstanceTransformer> = this.graphqlTransformers,
    executionInputTransformers: List<GraphQLJavaExecutionInputTransformer> = this.executionInputTransformers
): GraphQLJavaConfiguration {
    return GraphQLJavaConfiguration(
        schemaTransformers = schemaTransformers,
        graphqlTransformers = graphqlTransformers,
        executionInputTransformers = executionInputTransformers
    )
}

/* ---------------------------------------------- */

/**
 * A block of code invoked to fill in options in
 * [GraphQLJavaMutableConfiguration].
 */
typealias GraphQLJavaConfigurationBlock =
        GraphQLJavaMutableConfiguration.() -> Unit

/**
 * Construct a new [GraphQLJavaConfiguration] with
 * the given builder [block].
 *
 * @param block the builder block.
 * @since 2.0.0
 */
fun GraphQLJavaConfiguration(
    block: GraphQLJavaConfigurationBlock = {}
): GraphQLJavaConfiguration {
    val element = GraphQLJavaMutableConfiguration()
    element.apply(block)
    return element.copy()
}

/* ---------------------------------------------- */

/**
 * Add the given [transformer] to configure the
 * `java-graphql` schema after being translated
 * and before being used.
 */
fun GraphQLJavaMutableConfiguration.transformSchema(
    transformer: GraphQLJavaSchemaTransformer
) {
    schemaTransformers += transformer
}

/**
 * Add the given [transformer] to configure the
 * `java-graphql` instance after being translated
 * and before being used.
 */
fun GraphQLJavaMutableConfiguration.transformInstance(
    transformer: GraphQLJavaInstanceTransformer
) {
    graphqlTransformers += transformer
}

/**
 * Add the given [transformer] to configure the
 * `java-graphql` input after being translated
 * and before being used.
 */
fun GraphQLJavaMutableConfiguration.transformInput(
    transformer: GraphQLJavaExecutionInputTransformer
) {
    executionInputTransformers += transformer
}

/* ============================================== */
