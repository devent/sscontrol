package com.anrisoftware.sscontrol.core.groovy

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.resources.ResourcesModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.core.api.ServiceException
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
        shouldFailWith(ServiceException) { bean.beanName "bean name" }
    }

    @Test
    void "set statement no name"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanName"
        bean.map.setAllowValue "beanName", false
        shouldFailWith(ServiceException) { bean.map.putValue "beanName", "bean name" }
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
    void "set statement map"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.addAllowedKeys "beanMap", "aaa", "bbb"
        bean.map.putMapValue "beanMap", "aaa", "foo"
        bean.map.putMapValue "beanMap", "bbb", "bar"
        assert bean.map.mapValue("beanMap", "aaa") == "foo"
        assert bean.map.mapValue("beanMap", "bbb") == "bar"
        shouldFailWith(ServiceException) { bean.map.putMapValue "beanMap", "ccc", "baz" }
    }

    @Test
    void "set statement map invalid key"() {
        Bean bean = injector.getInstance Bean
        bean.map.addAllowed "beanMap"
        bean.map.addAllowedKeys "beanMap", "aaa"
        bean.map.putMapValue "beanMap", "aaa", "foo"
        shouldFailWith(ServiceException) { bean.map.putMapValue "beanMap", "bbb", "bar" }
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
