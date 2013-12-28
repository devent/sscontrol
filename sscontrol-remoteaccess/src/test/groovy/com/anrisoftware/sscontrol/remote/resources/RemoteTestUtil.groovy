/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.resources

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.google.inject.Guice.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.google.inject.Injector

/**
 * Creates the test environment.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RemoteTestUtil {

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    ServicesRegistry registry

    SscontrolServiceLoader loader

    File tmpdir

    Map variables

    @Before
    void createTemp() {
        tmpdir = tmp.newFolder("remote")
        variables = [tmp: tmpdir.absoluteFile]
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
    }

    static Injector createInjector() {
        createInjector(new CoreModule(), new CoreResourcesModule(), new ServiceModule())
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}
