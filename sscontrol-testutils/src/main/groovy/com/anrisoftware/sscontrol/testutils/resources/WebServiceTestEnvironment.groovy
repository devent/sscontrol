/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-testutils.
 *
 * sscontrol-testutils is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-testutils is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-testutils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.testutils.resources

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.anrisoftware.sscontrol.core.service.ThreadsPropertiesProvider
import com.anrisoftware.sscontrol.httpd.service.HttpdPreScript
import com.anrisoftware.sscontrol.httpd.service.HttpdPreScriptModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Creates the test environment for web service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WebServiceTestEnvironment {

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    static HttpdPreScript preScript

    static ThreadsPropertiesProvider threadsPropertiesProvider

    static PropertiesThreadsFactory threadsFactory

    static Threads threads

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    ServicesRegistry registry

    SscontrolServiceLoader loader

    File tmpdir

    Map variables

    @Before
    void createTemp() {
        tmpdir = tmp.newFolder("redmine")
        variables = [tmp: tmpdir.absoluteFile]
    }

    @Before
    void createRegistry() {
        registry = injector.getInstance ServicesRegistry
        loader = loaderFactory.create registry, variables
        loader.setParent injector
        loader.setThreads threads
    }

    @BeforeClass
    static void createFactories() {
        injector = createInjector()
        loaderFactory = injector.getInstance ServiceLoaderFactory
        preScript = injector.getInstance HttpdPreScript
        threadsPropertiesProvider = injector.getInstance ThreadsPropertiesProvider
        threadsFactory = injector.getInstance PropertiesThreadsFactory
        threads = createThreads()
    }

    static createThreads() {
        def threads = threadsFactory.create();
        threads.setProperties(threadsPropertiesProvider.get());
        threads.setName("script");
        return threads
    }

    static Injector createInjector() {
        Guice.createInjector(
                new CoreModule(), new CoreResourcesModule(),
                new ServiceModule(), new HttpdPreScriptModule())
    }

    /**
     * Attach the logger for RunCommands commands collection. The commands
     * are logged into the file {@code "<prefix>/runcommands.log"}
     */
    static attachRunCommandsLog(File prefix) {
        ScriptTestEnvironment.attachRunCommandsLog prefix
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}
