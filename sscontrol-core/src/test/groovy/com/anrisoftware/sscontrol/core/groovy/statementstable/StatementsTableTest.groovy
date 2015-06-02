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
package com.anrisoftware.sscontrol.core.groovy.statementstable

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapModule;
import com.anrisoftware.sscontrol.core.listproperty.ListPropertyModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see StatementsTable
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatementsTableTest {

    @Test
    void "statement table"() {
        BeanTable bean = injector.getInstance BeanTable
        bean.map.addAllowed "statement"
        bean.map.addAllowedKeys "statement", "keyFoo", "keyBar"
        bean.statement "aaa", keyFoo: "foo"
        bean.statement "bbb", keyFoo: "foo", keyBar: "bar"
        assert bean.map.tableKeys("statement", "keyFoo") == [aaa: "foo", bbb: "foo"]
        assert bean.map.tableKeys("statement", "keyBar") == [bbb: "bar"]
        assert bean.map.tableValues("statement").containsAll(["aaa", "bbb"])
    }

    @Test
    void "statement table, arbitrary"() {
        BeanTable bean = injector.getInstance BeanTable
        bean.map.addAllowed "statement"
        bean.map.setAllowArbitraryKeys true, "statement"
        bean.statement "aaa", keyFoo: "foo"
        bean.statement "bbb", keyFoo: "foo", keyBar: "bar"
        assert bean.map.tableKeys("statement", "keyFoo") == [aaa: "foo", bbb: "foo"]
        assert bean.map.tableKeys("statement", "keyBar") == [bbb: "bar"]
        assert bean.map.tableValues("statement").containsAll(["aaa", "bbb"])
    }

    @Test
    void "statement table with just table value"() {
        BeanTable bean = injector.getInstance BeanTable
        bean.map.addAllowed "statement"
        bean.map.addAllowedKeys "statement", "keyFoo", "keyBar"
        bean.statement "aaa"
        assert bean.map.tableKeys("statement", "keyFoo") == null
        assert bean.map.tableValues("statement").containsAll(["aaa"])
    }

    @Test
    void "statement table as list"() {
        BeanTable bean = injector.getInstance BeanTable
        bean.map.addAllowed "statement"
        bean.map.addAllowedKeys "statement", "keyFoo", "keyBar"
        bean.statement "aaa", keyFoo: "foo1"
        bean.statement "bbb", keyFoo: "foo2, foo3", keyBar: "bar"
        assert bean.map.tableKeysAsList("statement", "keyFoo") == [aaa: ["foo1"], bbb: ["foo2", "foo3"]]
        assert bean.map.tableKeysAsList("statement", "keyBar") == [bbb: ["bar"]]
    }

    static Injector injector

    static StatementsMapFactory factory

    @BeforeClass
    static void createFactory() {
        toStringStyle
        injector = Guice.createInjector(
                new StatementsMapModule(),
                new ResourcesModule(),
                new ListPropertyModule(),
                new TextsResourcesDefaultModule())
        factory = injector.getInstance StatementsMapFactory
    }
}
