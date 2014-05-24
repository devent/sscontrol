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
package com.anrisoftware.sscontrol.scripts.changefilemod

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.changefilemod.Ubuntu_10_04_Resources.*
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
 * @see ChangeFileOwner
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ChangeFileModTest {

    @Test
    void "change mode"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = changeFileModFactory.create(
                log: log,
                command: chmodCommand.asFile(tmpdir),
                files: [
                    fileFooCommand.asFile(tmpdir),
                    fileBarCommand.asFile(tmpdir)
                ],
                mod: "+w",
                this, threads)()
        assertStringContent chmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodOutExpected.toString()
    }

    @Test
    void "change mode recursive"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = changeFileModFactory.create(
                log: log,
                command: chmodCommand.asFile(tmpdir),
                files: [
                    fileFooCommand.asFile(tmpdir),
                    fileBarCommand.asFile(tmpdir)
                ],
                mod: "+w",
                recursive: true,
                this, threads)()
        assertStringContent chmodRecursiveOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chmodRecursiveOutExpected.toString()
    }

    static Injector injector

    static ChangeFileModFactory changeFileModFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new ChangeFileModModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
        changeFileModFactory = injector.getInstance ChangeFileModFactory
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
