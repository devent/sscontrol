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
package com.anrisoftware.sscontrol.scripts.localuserinfo

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.localuserinfo.Ubuntu_10_04_Resources.*
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
 * @see LocalUserInfo
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LocalUserInfoTest {

    @Test
    void "check user"() {
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
    void "check no user"() {
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

    static LocalUserInfoFactory localUserInfoFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new LocalUserInfoModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
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
