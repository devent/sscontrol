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
package com.anrisoftware.sscontrol.scripts.processinfo

import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessState.*
import static com.anrisoftware.sscontrol.scripts.processinfo.Ubuntu_10_04_Resources.*
import groovy.util.logging.Slf4j

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * @see ProcessInfo
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ProcessInfoTest extends ProcessInfoDependencies {

    @Test
    void "process info"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def info = dep.processInfoFactory.create log: log, command: psCommand.asFile(tmpdir), search: ".*web_004.*", this, dep.threads
        info()

        assert info.processFound == true
        assert info.processCommandName == "gitit"
        assert info.processUser == "web_004"
        assert info.processGroup == "web_004"
        assert info.processUserId == 2004
        assert info.processGroupId == 2004
        assert info.processId == 3795
        assert info.processCommandArgs =~ /.*thin server.*/
        assert info.processStates.containsAll([INT_SLEEP])
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()
}
