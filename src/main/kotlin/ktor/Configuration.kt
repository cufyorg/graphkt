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
package org.cufy.kaguya.ktor

import graphql.ExecutionInput
import graphql.ExecutionResult
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import org.cufy.kaguya.GraphQLBuilder
import org.cufy.kaguya.GraphQLContextBuilder
import org.cufy.kaguya.GraphQLSchemaBuilder

/**
 * A configuration object to be passed to a graphql
 * plugin.
 *
 * @author LSafer
 * @since 1.0.0
 */
open class Configuration {
    /**
     * Set to true to enable GraphiQL mode.
     */
    var graphiql: Boolean = true

    /**
     * A function to be invoked after the
     * construction of the configuration instance.
     *
     * @since 1.0.0
     */
    var configurationBlock: Configuration.() -> Unit = {}

    /**
     * A function to be invoked when constructing
     * the schema instance.
     *
     * @since 1.0.0
     */
    var schemaBlock: GraphQLSchemaBuilder.() -> Unit = {}

    /**
     * A function to be invoked when constructing
     * the graphql instance.
     */
    var graphqlBlock: GraphQLBuilder.() -> Unit = {}

    /**
     * A function to be invoked when constructing
     * a new context.
     *
     * @since 1.0.0
     */
    var contextBlock: suspend GraphQLContextBuilder.(
        PipelineContext<Unit, ApplicationCall>
    ) -> Unit = {}

    /**
     * A function to be invoked when constructing
     * a new execution input.
     *
     * @since 1.0.0
     */
    var executionInputBlock: suspend ExecutionInput.Builder.(
        PipelineContext<Unit, ApplicationCall>
    ) -> Unit = {}

    /**
     * A function to be invoked after execution is
     * finished.
     *
     * @since 1.1.0
     */
    var executionResultBlock: suspend PipelineContext<Unit, ApplicationCall>.(
        ExecutionResult
    ) -> Unit = { }
}

/**
 * Execute the given [block] after the construction
 * of the configuration object.
 *
 * @since 1.0.0
 */
fun Configuration.plugin(
    block: Configuration.() -> Unit = {}
) {
    configurationBlock.let {
        configurationBlock = {
            it()
            block()
        }
    }
}

/**
 * Execute the given [block] when constructing the
 * graphql schema.
 *
 * @since 1.0.0
 */
fun Configuration.schema(
    block: GraphQLSchemaBuilder.() -> Unit = {}
) {
    schemaBlock.let {
        schemaBlock = {
            it()
            block()
        }
    }
}

/**
 * Execute the given [block] when constructing the
 * graphql instance.
 *
 * @since 1.0.0
 */
fun Configuration.instance(
    block: GraphQLBuilder.() -> Unit = {}
) {
    graphqlBlock.let {
        graphqlBlock = {
            it()
            block()
        }
    }
}

/**
 * Execute the given [block] when constructing a
 * new graphql context.
 *
 * @since 1.0.0
 */
fun Configuration.context(
    block: suspend GraphQLContextBuilder.(
        PipelineContext<Unit, ApplicationCall>
    ) -> Unit = {}
) {
    contextBlock.let {
        contextBlock = {
            it(it)
            block(it)
        }
    }
}

/**
 * Execute the given [block] when constructing a
 * new graphql execution input.
 *
 * @since 1.0.0
 */
fun Configuration.executionInput(
    block: suspend ExecutionInput.Builder.(
        PipelineContext<Unit, ApplicationCall>
    ) -> Unit = {}
) {
    executionInputBlock.let {
        executionInputBlock = {
            it(it)
            block(it)
        }
    }
}

/**
 * Execute the given [block] after execution is finished.
 *
 * @since 1.1.0
 */
fun Configuration.executionResult(
    block: suspend PipelineContext<Unit, ApplicationCall>.(
        ExecutionResult
    ) -> Unit = { }
) {
    executionResultBlock.let {
        executionResultBlock = {
            it(it)
            block(it)
        }
    }
}
