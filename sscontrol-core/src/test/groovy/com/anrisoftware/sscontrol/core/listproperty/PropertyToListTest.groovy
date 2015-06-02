/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.listproperty

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.google.inject.Guice.createInjector
import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test

import com.google.inject.Injector

/**
 * @see PropertyToList
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@Slf4j
class PropertyToListTest {

    @Test
    void "values to list"() {
        def cases = [
            [property: "a", expected: ["a"]],
            [property: "a, b, c", expected: ["a", "b", "c"]],
            [property: ["a", "b", "c"], expected: ["a", "b", "c"]],
            [property: Locale.GERMAN, expected: [Locale.GERMAN]],
            [property: [Locale.GERMAN, Locale.US], expected: [Locale.GERMAN, Locale.US]],
        ]
        cases.eachWithIndex { it, k ->
            def list = factory.create it.property list
            log.info "{}.case: property '{}', expect {}", k, it.property, it.expected
            assert list.size() == it.expected.size()
            assert list.containsAll(it.expected)
        }
    }

    static Injector injector

    static PropertyToListFactory factory

    @BeforeClass
    static void createFactory() {
        toStringStyle
        this.injector = createInjector new ListPropertyModule()
        this.factory = injector.getInstance PropertyToListFactory
    }
}
