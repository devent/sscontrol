/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.core.list.ListModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see StatementsMap
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatementsMapTest {

    @Test
    void "statement name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanName"
        bean.map.setAllowValue "beanName", true
        bean.beanName "bean name"
        assert bean.map.value("beanName") == "bean name"
    }

    @Test
    void "set statement name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanName"
        bean.map.setAllowValue "beanName", true
        bean.map.putValue "beanName", "bean name"
        assert bean.map.value("beanName") == "bean name"
    }

    @Test
    void "statement no name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanName"
        bean.map.setAllowValue "beanName", false
        shouldFailWith(StatementsException) { bean.beanName "bean name" }
    }

    @Test
    void "set statement no name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanName"
        bean.map.setAllowValue "beanName", false
        shouldFailWith(StatementsException) { bean.map.putValue "beanName", "bean name" }
    }

    @Test
    void "statement list"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanList"
        bean.map.setAllowValue "beanList", true
        bean.beanList "foo, bar"
        assert bean.map.valueAsList("beanList") == ["foo", "bar"]
    }

    @Test
    void "statement map"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.addAllowedKeys "beanMap", "aaa", "bbb"
        bean.beanMap aaa: "foo", bbb: "bar"
        assert bean.map.mapValue("beanMap", "aaa") == "foo"
        assert bean.map.mapValue("beanMap", "bbb") == "bar"
    }

    @Test
    void "statement map with name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.setAllowValue "beanMap", true
        bean.map.addAllowedKeys "beanMap", "aaa", "bbb"
        bean.beanMap aaa: "foo", bbb: "bar", "value"
        assert bean.map.value("beanMap") == "value"
        assert bean.map.mapValue("beanMap", "aaa") == "foo"
        assert bean.map.mapValue("beanMap", "bbb") == "bar"
    }

    @Test
    void "statement map with name, multi"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.setAllowMultiValue "beanMap", true
        bean.map.addAllowedMultiKeys "beanMap", "aaa", "bbb"
        bean.beanMap aaa: "foo1", bbb: "bar1", "value1"
        bean.beanMap aaa: "foo2", bbb: "bar2", "value2"
        assert bean.map.value("beanMap").containsAll(["value1", "value2"])
        assert bean.map.mapValue("beanMap", "aaa").containsAll(["foo1", "foo2"])
        assert bean.map.mapValue("beanMap", "bbb").containsAll(["bar1", "bar2"])
    }

    @Test
    void "set statement map"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.addAllowedKeys "beanMap", "aaa", "bbb"
        bean.map.putMapValue "beanMap", "aaa", "foo"
        bean.map.putMapValue "beanMap", "bbb", "bar"
        assert bean.map.mapValue("beanMap", "aaa") == "foo"
        assert bean.map.mapValue("beanMap", "bbb") == "bar"
        shouldFailWith(StatementsException) { bean.map.putMapValue "beanMap", "ccc", "baz" }
    }

    @Test
    void "set statement map invalid key"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.addAllowedKeys "beanMap", "aaa"
        bean.map.putMapValue "beanMap", "aaa", "foo"
        shouldFailWith(StatementsException) { bean.map.putMapValue "beanMap", "bbb", "bar" }
        assert bean.map.mapValue("beanMap", "aaa") == "foo"
    }

    static Injector injector

    static StatementsMapFactory factory

    @BeforeClass
    static void createFactory() {
        toStringStyle
        injector = Guice.createInjector(
                new StatementsMapModule(),
                new ResourcesModule(),
                new ListModule(),
                new TextsResourcesDefaultModule())
        factory = injector.getInstance StatementsMapFactory
    }
}
