/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.ProfileService
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Load a profile from a Groovy script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LoadProfileTest {

    @Test
    void "load profile script"() {
        loader.loadService ubuntu1004Profile, null
        assert registry.serviceNames.toString() == "[profile]"
        assert registry.getService("profile").size() == 1
        ProfileService profile = registry.getService("profile")[0]
        assert profile.entryNames.toString() == "[hosts, hostname, httpd]"

        ProfileProperties system = profile.getEntry("hosts")
        assert system.keys.size() == 8
        assert system.get("echo_command") == "echo"
        assert system.get("install_command") == "aptitude update && aptitude install %s"
        assert system.get("install_command", "aaa") == "aptitude update && aptitude install aaa"
        assert system.get("set_enabled") == true
        assert system.get("set_gstring") == "gstring test"
        assert system.get("set_multiple") == ["aaa", "bbb"]
        assert system.get("set_number") == 11
        assert system.get("set_method_enabled") == true
        assert system.get("property_with_variables") == "one two three"

        ProfileProperties hostname = profile.getEntry("hostname")
        assert hostname.keys.size() == 2
        assert hostname.get("echo_command") == "echo"
        assert hostname.get("install_command") == "aptitude update && aptitude install %s"

        ProfileProperties httpd = profile.getEntry("httpd")
        assert httpd.keys.size() == 1
        assert httpd.get("service").idapache2 == "apache"
        assert httpd.get("service").idproxy == "nginx"
    }

    static ubuntu1004Profile = LoadProfileTest.class.getResource("Ubuntu_10_04Profile.groovy")

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    ServicesRegistry registry

    SscontrolServiceLoader loader

    Map variables

    @BeforeClass
    static void createFactories() {
        injector = createInjector()
        loaderFactory = injector.getInstance ServiceLoaderFactory
    }

    static createInjector() {
        Guice.createInjector(
                new CoreModule(), new CoreResourcesModule(), new ServiceModule())
    }

    @Before
    void createRegistry() {
        variables = [one: "one", two: "two", three: "three"]
        registry = injector.getInstance ServicesRegistry
        loader = loaderFactory.create registry, variables
        loader.setParent injector
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}
