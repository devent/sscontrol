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
package com.anrisoftware.sscontrol.scripts.localuser

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.localuser.Resources.*
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
class LocalUserTest {

    @Test
    void "change password on Ubuntu"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
        copyTestFiles tmpdir
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
        copyTestFiles tmpdir
        def repositoryEnabled = localChangePasswordFactory.create(
                log: log,
                command: chpasswdCommand.asFile(tmpdir),
                name: "redhat",
                password: "foopass",
                userName: "foo",
                this, threads)()
        assertFileContent redhatChpasswdCommandOutExpected.asFile(tmpdir), redhatChpasswdCommandOutExpected
    }

    @Test
    void "change groups"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        def repositoryEnabled = localChangeUserFactory.create(
                log: log,
                command: usermodCommand.asFile(tmpdir),
                userName: "foo",
                groups: ["foogroup", "bar"],
                this, threads)()
        assertFileContent groupsUsermodOutExpected.asFile(tmpdir), groupsUsermodOutExpected
    }

    @Test
    void "append groups"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        def repositoryEnabled = localChangeUserFactory.create(
                log: log,
                command: usermodCommand.asFile(tmpdir),
                userName: "foo",
                groups: ["foogroup", "bar"],
                append: true,
                this, threads)()
        assertFileContent groupsAppendUsermodOutExpected.asFile(tmpdir), groupsAppendUsermodOutExpected
    }

    @Test
    void "mod shell"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        def repositoryEnabled = localChangeUserFactory.create(
                log: log,
                command: usermodCommand.asFile(tmpdir),
                userName: "foo",
                shell: "/bin/bash",
                this, threads)()
        assertFileContent shellUsermodOutExpected.asFile(tmpdir), shellUsermodOutExpected
    }

    @Test
    void "not contains group"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        groupsFile.createFile tmpdir
        def repositoryEnabled = localGroupAddFactory.create(
                log: log,
                command: groupAddCommand.asFile(tmpdir),
                groupsFile: groupsFile.asFile(tmpdir),
                groupId: "100",
                groupName: "test",
                this, threads)()
        assertFileContent groupAddOutExpected.asFile(tmpdir), groupAddOutExpected
    }

    @Test
    void "group system"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        groupsFile.createFile tmpdir
        def repositoryEnabled = localGroupAddFactory.create(
                log: log,
                command: groupAddCommand.asFile(tmpdir),
                groupsFile: groupsFile.asFile(tmpdir),
                groupId: "100",
                groupName: "test",
                systemGroup: true,
                this, threads)()
        assertFileContent groupSystemAddOutExpected.asFile(tmpdir), groupSystemAddOutExpected
    }

    @Test
    void "contains group"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
        groupsFileWithGroup.createFile tmpdir
        def repositoryEnabled = localGroupAddFactory.create(
                log: log,
                command: groupAddCommand.asFile(tmpdir),
                groupsFile: groupsFileWithGroup.asFile(tmpdir),
                groupId: "100",
                groupName: "test",
                this, threads)()
        assert !withGroupGroupAddOutExpected.asFile(tmpdir).isFile()
    }

    @Test
    void "user add, not contains user"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
    void "user add, system user"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
    void "user add, user home dir"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
    void "user add, user shell"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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
    void "user add, contains user"() {
        def tmpdir = tmp.newFolder()
        copyTestFiles tmpdir
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

    @Test
    void "user info, check user"() {
        def tmpdir = tmp.newFolder()
        idCommand.createCommand tmpdir
        def info = localUserInfoFactory.create(
                log: log,
                command: idCommand.asFile(tmpdir),
                userName: "devent",
                this, threads)()
        assert info.uid == 1000
        assert info.userName == "devent"
        assert info.gid == 1000
        assert info.groupName == "devent"
        assert info.groups.size() == 2
        assert info.groups["devent"] == 1000
        assert info.groups["wheel"] == 10
    }

    @Test
    void "user info, check no user"() {
        def tmpdir = tmp.newFolder()
        idNoUserCommand.createCommand tmpdir
        def info = localUserInfoFactory.create(
                log: log,
                command: idNoUserCommand.asFile(tmpdir),
                userName: "aaa",
                this, threads)()
        assert info.uid == null
        assert info.userName == null
        assert info.gid == null
        assert info.groupName == null
        assert info.groups.size() == 0
    }

    static Injector injector

    static LocalChangePasswordFactory localChangePasswordFactory

    static LocalChangeUserFactory localChangeUserFactory

    static LocalGroupAddFactory localGroupAddFactory

    static LocalUserAddFactory localUserAddFactory

    static LocalUserInfoFactory localUserInfoFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new LocalUserModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        localChangePasswordFactory = injector.getInstance LocalChangePasswordFactory
        localChangeUserFactory = injector.getInstance LocalChangeUserFactory
        localGroupAddFactory = injector.getInstance LocalGroupAddFactory
        localUserAddFactory = injector.getInstance LocalUserAddFactory
        localUserInfoFactory = injector.getInstance LocalUserInfoFactory
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
