/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.killprocess

import javax.inject.Inject

import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.worker.STWorkerModule
import com.anrisoftware.sscontrol.scripts.unix.TestThreadsPropertiesProvider
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see KillProcessTest
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class KillProcessDependencies {

    @Inject
    KillProcessFactory killProcessFactory

    @Inject
    PropertiesThreadsFactory threadsFactory

    @Inject
    TestThreadsPropertiesProvider threadsPoolProvider

    Threads threads

    static Injector injector = Guice.createInjector(
    new KillProcessModule(),
    new UnixScriptsModule(),
    new UnixScriptsModule.ExecCommandModule(),
    new PropertiesThreadsModule(),
    new TemplatesResourcesModule(),
    new TemplatesDefaultMapsModule(),
    new STWorkerModule(),
    new STDefaultPropertiesModule()
    )

    static KillProcessDependencies dep

    @BeforeClass
    static void setupDependencies() {
        this.dep = injector.getInstance KillProcessDependencies
    }

    @Before
    void createThreadsPool() {
        dep.threads = dep.threadsFactory.create();
        dep.threads.setProperties dep.threadsPoolProvider.get()
        dep.threads.setName("script");
    }
}
