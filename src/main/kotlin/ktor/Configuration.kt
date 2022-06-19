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
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import org.cufy.kaguya.GraphQLContextScope
import org.cufy.kaguya.GraphQLSchemaScope
import org.cufy.kaguya.GraphQLScope

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
     * A function to be invoked when constructing
     * the schema instance.
     *
     * @since 1.0.0
     */
    var schemaBlock: GraphQLSchemaScope.() -> Unit = {}

    /**
     * A function to be invoked when constructing
     * the graphql instance.
     */
    var graphqlBlock: GraphQLScope.() -> Unit = {}

    /**
     * A function to be invoked when constructing
     * a new context.
     *
     * @since 1.0.0
     */
    var contextBlock: suspend GraphQLContextScope.(
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
     * Execute the given [block] when constructing the
     * graphql schema.
     *
     * @since 1.0.0
     */
    fun schema(
        block: GraphQLSchemaScope.() -> Unit = {}
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
    fun instance(
        block: GraphQLScope.() -> Unit = {}
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
    fun context(
        block: GraphQLContextScope.(
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
    fun executionInput(
        block: ExecutionInput.Builder.(
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
}
