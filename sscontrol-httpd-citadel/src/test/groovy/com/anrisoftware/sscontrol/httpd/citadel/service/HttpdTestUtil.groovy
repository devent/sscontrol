/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.service

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.anrisoftware.sscontrol.httpd.service.HttpdPreScript
import com.anrisoftware.sscontrol.httpd.service.HttpdPreScriptModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Creates the httpd service for testing.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdTestUtil {

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    static HttpdPreScript preScript

    File tmpdir

    Map variables

    ServicesRegistry registry

    SscontrolServiceLoader loader

    @Before
    void createTemp() {
        tmpdir = File.createTempDir this.class.simpleName, null
        variables = [tmp: tmpdir.absoluteFile]
    }

    @After
    void deleteTemp() {
        tmpdir.deleteDir()
    }

    @Before
    void createRegistry() {
        registry = injector.getInstance ServicesRegistry
        loader = loaderFactory.create registry, variables
        loader.setParent injector
    }

    @BeforeClass
    static void createFactories() {
        injector = createInjector()
        loaderFactory = injector.getInstance ServiceLoaderFactory
        preScript = injector.getInstance HttpdPreScript
    }

    static Injector createInjector() {
        Guice.createInjector(
                new CoreModule(), new CoreResourcesModule(),
                new ServiceModule(), new HttpdPreScriptModule())
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}

