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
package com.anrisoftware.sscontrol.scripts.killprocess

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.killprocess.KillType.*
import static com.anrisoftware.sscontrol.scripts.killprocess.Ubuntu_10_04_Resources.*
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
class KillProcessTest extends KillProcessDependencies {

    @Test
    void "terminate process"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def info = dep.killProcessFactory.create(
                log: log,
                command: killCommand.asFile(tmpdir),
                process: 1345,
                this,
                dep.threads)
        info()
        assertFileContent killTermOutExpected.asFile(tmpdir), killTermOutExpected
    }

    @Test
    void "kill process"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        def info = dep.killProcessFactory.create(
                log: log,
                command: killCommand.asFile(tmpdir),
                process: 1345,
                type: KILL,
                this,
                dep.threads)
        info()
        assertFileContent killKillOutExpected.asFile(tmpdir), killKillOutExpected
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()
}
