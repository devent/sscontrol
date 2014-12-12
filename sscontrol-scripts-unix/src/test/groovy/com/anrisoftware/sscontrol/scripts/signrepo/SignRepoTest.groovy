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
package com.anrisoftware.sscontrol.scripts.signrepo

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.signrepo.Ubuntu_10_04_Resources.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

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
 * @see SignRepo
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SignRepoTest {

    @Test
    void "sign repo"() {
        def tmpdir = tmp.newFolder()
        aptkeyCommand.createCommand tmpdir
        key.createFile tmpdir
        def repositoryEnabled = signRepoFactory.create(
                log: log,
                command: aptkeyCommand.asFile(tmpdir),
                key: key.asFile(tmpdir).toURI().toURL(),
                name: "nginx signing key <signing-key@nginx.com>",
                tmp: tmpdir,
                system: "ubuntu",
                this, threads)()
        assertStringContent aptkeyOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aptkeyOutExpected.toString()
    }

    @Test
    void "sign repo nginx"() {
        def tmpdir = tmp.newFolder()
        aptkeyNginxCommand.createCommand tmpdir
        key.createFile tmpdir
        def repositoryEnabled = signRepoFactory.create(
                log: log,
                command: aptkeyNginxCommand.asFile(tmpdir),
                key: key.asFile(tmpdir).toURI().toURL(),
                name: "nginx signing key <signing-key@nginx.com>",
                tmp: tmpdir,
                system: "ubuntu",
                this, threads)()
        assertStringContent aptkeyNginxOutExpected.replaced(tmpdir, tmpdir, "/tmp"), aptkeyNginxOutExpected.toString()
    }

    static Injector injector

    static SignRepoFactory signRepoFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new SignRepoModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
        signRepoFactory = injector.getInstance SignRepoFactory
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
