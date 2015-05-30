/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04

import javax.inject.Inject

import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.sscontrol.scripts.locale.core.LocaleCoreModule
import com.anrisoftware.sscontrol.scripts.locale.ubuntu.UbuntuInstallLocaleModule
import com.anrisoftware.sscontrol.scripts.unix.TestThreadsPropertiesProvider
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see Ubuntu_12_04_InstallLocaleTest
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuDependencies {

    @Inject
    Ubuntu_12_04_InstallLocaleFactory installLocaleFactory

    @Inject
    PropertiesThreadsFactory threadsFactory

    @Inject
    TestThreadsPropertiesProvider threadsPoolProvider

    Threads threads

    static Injector injector = Guice.createInjector(
    new Ubuntu_12_04_InstallLocaleModule(),
    new UbuntuInstallLocaleModule(),
    new LocaleCoreModule(),
    new UnixScriptsModule(),
    new UnixScriptsModule.UnixScriptsDefaultsModule(),
    new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
    new PropertiesThreadsModule())

    static UbuntuDependencies dep

    @BeforeClass
    static void setupDependencies() {
        this.dep = injector.getInstance UbuntuDependencies
    }

    @Before
    void createThreadsPool() {
        dep.threads = dep.threadsFactory.create();
        dep.threads.setProperties dep.threadsPoolProvider.get()
        dep.threads.setName("script");
    }
}