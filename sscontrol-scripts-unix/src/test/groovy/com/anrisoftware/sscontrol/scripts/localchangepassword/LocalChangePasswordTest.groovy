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
package com.anrisoftware.sscontrol.scripts.localchangepassword

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.localchangepassword.Ubuntu_10_04_Resources.*
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
 * @see LocalChangePassword
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LocalChangePasswordTest {

    @Test
    void "change password on Ubuntu"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = localChangePasswordFactory.create(
                log: log,
                command: chpasswdCommand.asFile(tmpdir),
                name: "ubuntu",
                password: "foopass",
                userName: "foo",
                this, threads)()
        assertFileContent ubuntuChpasswdCommandOutExpected.asFile(tmpdir), ubuntuChpasswdCommandOutExpected
    }

    @Test
    void "change password on Debian"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = localChangePasswordFactory.create(
                log: log,
                command: chpasswdCommand.asFile(tmpdir),
                name: "debian",
                password: "foopass",
                userName: "foo",
                this, threads)()
        assertFileContent ubuntuChpasswdCommandOutExpected.asFile(tmpdir), ubuntuChpasswdCommandOutExpected
    }

    @Test
    void "change password on Redhat"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def repositoryEnabled = localChangePasswordFactory.create(
                log: log,
                command: chpasswdCommand.asFile(tmpdir),
                name: "redhat",
                password: "foopass",
                userName: "foo",
                this, threads)()
        assertFileContent redhatChpasswdCommandOutExpected.asFile(tmpdir), redhatChpasswdCommandOutExpected
    }

    static Injector injector

    static LocalChangePasswordFactory localChangePasswordFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new LocalChangePasswordModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        localChangePasswordFactory = injector.getInstance LocalChangePasswordFactory
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
