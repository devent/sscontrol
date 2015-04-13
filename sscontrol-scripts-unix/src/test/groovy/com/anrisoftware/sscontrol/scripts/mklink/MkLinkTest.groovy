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
package com.anrisoftware.sscontrol.scripts.mklink

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.mklink.Ubuntu_10_04_Resources.*
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
 * @see MkLink
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MkLinkTest {

    @Test
    void "mk link"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = mkLinkFactory.create(
                log: log,
                command: lnCommand.asFile(tmpdir),
                files: sourceA.asFile(tmpdir),
                targets: targetA.asFile(tmpdir),
                this, threads)()
        assertStringContent lnOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnOutExpected.toString()
    }

    @Test
    void "mk link override"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = mkLinkFactory.create(
                log: log,
                command: lnCommand.asFile(tmpdir),
                files: sourceA.asFile(tmpdir),
                targets: targetA.asFile(tmpdir),
                override: true,
                this, threads)()
        assertStringContent lnOverrideOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnOverrideOutExpected.toString()
    }

    @Test
    void "mk links"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = mkLinkFactory.create(
                log: log,
                command: lnCommand.asFile(tmpdir),
                files: [
                    sourceA.asFile(tmpdir),
                    sourceB.asFile(tmpdir)
                ],
                targets: [
                    targetA.asFile(tmpdir),
                    targetB.asFile(tmpdir)
                ],
                this, threads)()
        assertStringContent lnsOutExpected.replaced(tmpdir, tmpdir, "/tmp"), lnsOutExpected.toString()
    }

    static Injector injector

    static MkLinkFactory mkLinkFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new MkLinkModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        mkLinkFactory = injector.getInstance MkLinkFactory
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
