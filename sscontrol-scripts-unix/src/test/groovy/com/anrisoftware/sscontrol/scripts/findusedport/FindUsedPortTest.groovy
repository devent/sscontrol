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
package com.anrisoftware.sscontrol.scripts.findusedport

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.findusedport.Ubuntu_10_04_Resources.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.sscontrol.scripts.unix.TestThreadsPropertiesProvider
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see FindUsedPort
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class FindUsedPortTest {

    @Test
    void "find ports"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def services = findUsedPortFactory.create(
                log: log,
                command: netstatCommand.asFile(tmpdir),
                ports: [631, 123],
                this, threads)()
        log.info "Services: {}", services.services
        assert services.services.size() == 2
        assert services.services[631] == "cupsd"
        assert services.services[123] == "chronyd"
    }

    @Test
    void "find no ports"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def services = findUsedPortFactory.create(
                log: log,
                command: netstatCommand.asFile(tmpdir),
                ports: [999, 1001],
                this, threads)()
        log.info "Services: {}", services.services
        assert services.services.size() == 0
    }

    static Injector injector

    static FindUsedPortFactory findUsedPortFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new FindUsedPortModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        findUsedPortFactory = injector.getInstance FindUsedPortFactory
        threadsFactory = injector.getInstance PropertiesThreadsFactory
        threadsPoolProvider = injector.getInstance TestThreadsPropertiesProvider
    }

    @Before
    void createThreadsPool() {
        threads = threadsFactory.create();
        threads.setProperties threadsPoolProvider.get()
        threads.setName("script");
    }
}
