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
package org.cufy.graphkt.schema

/*
 Important Note: these interface might change in
 the future. It was made to make it easier to
 implement features for different kids of tweaks
 with less code and not to be used by regular
 users.
*/

/**
 * An instance with directives.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface WithDirectives {
    /**
     * The applied directives.
     *
     * @since 2.0.0
     */
    val directives: List<GraphQLDirective>
}

/**
 * An instance with a name and description.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface WithName : WithDescription {
    /**
     * The name of the element.
     */
    val name: String
}

/**
 * An instance with a description.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface WithDescription {
    /**
     * The description of the element.
     */
    val description: String
}

/**
 * An instance with argument definitions.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface WithArgumentDefinitions {
    /**
     * The arguments this element accepts.
     */
    val arguments: List<GraphQLArgumentDefinition<*>>
}

/**
 * An instance with arguments.
 *
 * Important Note: these interface might change in
 * the future. It was made to make it easier to
 * implement features for different kids of tweaks
 * with less code and not to be used by regular
 * users.
 *
 * @author LSafer
 * @since 2.0.0
 */
interface WithArguments {
    /**
     * The arguments passed to the definition.
     *
     * @since 2.0.0
     */
    val arguments: List<GraphQLArgument<*>>
}
