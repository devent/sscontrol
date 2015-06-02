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
package com.anrisoftware.sscontrol.core.groovy.bindingaddressstatements

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.core.groovy.bindingaddressstatements.BindingAddressesStatementsFactory;
import com.anrisoftware.sscontrol.core.groovy.bindingaddressstatements.BindingAddressesStatementsModule;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapModule
import com.anrisoftware.sscontrol.core.listproperty.ListPropertyModule
import com.google.inject.Guice

/**
 * @see BindingAddressesStatementsTable
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindingAddressesStatementsTest {

    @Test
    void "binding addresses"() {
        def service = this
        def name = "foo"
        def cases = [
            [statement: { it.bind "192.168.0.1", port: 80 }, requirePort: true, expected: "", expectedException: null],
            [statement: { it.bind "192.168.0.1" }, requirePort: false, expected: "", expectedException: null],
            [statement: { it.bind "*", port: 80 }, expected: "", requirePort: true, expectedException: null],
            [statement: { it.bind "192.168.0.1", ports: [8092, 8094]}, requirePort: true, expected: "", expectedException: null],
            [statement: { it.bind "bla", ports: [8092, 8094]}, requirePort: true, expected: "", expectedException: IllegalArgumentException]
        ]
        cases.each { usecase ->
            def binding = bindingAddressesFactory.create(service, name)
            binding.requirePort = usecase.requirePort
            if (!usecase.expectedException) {
                usecase.statement binding
            } else {
                shouldFailWith(usecase.expectedException) { usecase.statement binding }
            }
        }
    }

    static BindingAddressesStatementsFactory bindingAddressesFactory

    @BeforeClass
    static void createFactory() {
        def injector = Guice.createInjector(
                new BindingAddressesStatementsModule(),
                new StatementsMapModule(),
                new ResourcesModule(),
                new ListPropertyModule(),
                new TextsResourcesDefaultModule())
        bindingAddressesFactory = injector.getInstance BindingAddressesStatementsFactory
    }
}
