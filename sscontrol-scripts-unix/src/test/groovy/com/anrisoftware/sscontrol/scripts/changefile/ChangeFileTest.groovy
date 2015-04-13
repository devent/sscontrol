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
package com.anrisoftware.sscontrol.scripts.changefile

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.changefile.Resources.*
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
 * @see ChangeFileOwner
 * @see ChangeFileModule
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ChangeFileTest {

    @Test
    void "change mode"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
        copyTestFiles tmpdir
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

    @Test
    void "change owner"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        def repositoryEnabled = changeFileOwnerFactory.create(
                log: log,
                command: chownCommand.asFile(tmpdir),
                files: [
                    fileFooCommand.asFile(tmpdir),
                    fileBarCommand.asFile(tmpdir)
                ],
                owner: "foo",
                ownerGroup: "foogroup",
                this, threads)()
        assertStringContent chownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownOutExpected.toString()
    }

    @Test
    void "change owner recursive"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        def repositoryEnabled = changeFileOwnerFactory.create(
                log: log,
                command: chownCommand.asFile(tmpdir),
                files: [
                    fileFooCommand.asFile(tmpdir),
                    fileBarCommand.asFile(tmpdir)
                ],
                owner: "foo",
                ownerGroup: "foogroup",
                recursive: true,
                this, threads)()
        assertStringContent chownRecursiveOutExpected.replaced(tmpdir, tmpdir, "/tmp"), chownRecursiveOutExpected.toString()
    }

    static Injector injector

    static ChangeFileModFactory changeFileModFactory

    static ChangeFileOwnerFactory changeFileOwnerFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new ChangeFileModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        changeFileModFactory = injector.getInstance ChangeFileModFactory
        changeFileOwnerFactory = injector.getInstance ChangeFileOwnerFactory
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
