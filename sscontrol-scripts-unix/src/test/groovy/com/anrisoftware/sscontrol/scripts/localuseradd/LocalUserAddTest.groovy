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
package com.anrisoftware.sscontrol.scripts.localuseradd

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.localuseradd.Ubuntu_10_04_Resources.*
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
 * @see LocalUserAdd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LocalUserAddTest {

    @Test
    void "not contains user"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        passwdFile.createFile tmpdir
        def repositoryEnabled = localUserAddFactory.create(
                log: log,
                command: userAddCommand.asFile(tmpdir),
                usersFile: passwdFile.asFile(tmpdir),
                userId: "100",
                userName: "test",
                groupName: "test",
                this, threads)()
        assertFileContent userAddOutExpected.asFile(tmpdir), userAddOutExpected
    }

    @Test
    void "system user"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        passwdFile.createFile tmpdir
        def repositoryEnabled = localUserAddFactory.create(
                log: log,
                command: userAddCommand.asFile(tmpdir),
                usersFile: passwdFile.asFile(tmpdir),
                userId: "100",
                userName: "test",
                groupName: "test",
                systemUser: true,
                this, threads)()
        assertFileContent userSystemAddOutExpected.asFile(tmpdir), userSystemAddOutExpected
    }

    @Test
    void "user home dir"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        passwdFile.createFile tmpdir
        def repositoryEnabled = localUserAddFactory.create(
                log: log,
                command: userAddCommand.asFile(tmpdir),
                usersFile: passwdFile.asFile(tmpdir),
                userId: "100",
                userName: "test",
                groupName: "test",
                homeDir: "/var/test",
                this, threads)()
        assertFileContent homeDirUserAddOutExpected.asFile(tmpdir), homeDirUserAddOutExpected
    }

    @Test
    void "user shell"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        passwdFile.createFile tmpdir
        def repositoryEnabled = localUserAddFactory.create(
                log: log,
                command: userAddCommand.asFile(tmpdir),
                usersFile: passwdFile.asFile(tmpdir),
                userId: "100",
                userName: "test",
                groupName: "test",
                shell: "/bin/bash",
                this, threads)()
        assertFileContent shellUserAddOutExpected.asFile(tmpdir), shellUserAddOutExpected
    }

    @Test
    void "contains user"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        passwdFileWithUser.createFile tmpdir
        def repositoryEnabled = localUserAddFactory.create(
                log: log,
                command: userAddCommand.asFile(tmpdir),
                usersFile: passwdFileWithUser.asFile(tmpdir),
                userId: "100",
                userName: "test",
                groupName: "test",
                this, threads)()
        assert !withUserUserAddOutExpected.asFile(tmpdir).isFile()
    }

    static Injector injector

    static LocalUserAddFactory localUserAddFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new LocalUserAddModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
        localUserAddFactory = injector.getInstance LocalUserAddFactory
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
