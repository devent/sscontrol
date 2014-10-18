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
package com.anrisoftware.sscontrol.scripts.processinfo

import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessState.*

import org.junit.Test

/**
 * @see ProcessStateFormat
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProcessStateFormatTest extends ProcessInfoDependencies {

    @Test
    void "parse process states"() {
        def testCases = [
            [source: "Sl", states: [INT_SLEEP]],
            [source: "SZ", states: [INT_SLEEP, DEFUNCT]],
        ]
        testCases.each {
            def expected = dep.formatFactory.create().parse(it.source)
            assert it.states.containsAll(expected)
        }
    }

    @Test
    void "format process states"() {
        def testCases = [
            [states: [INT_SLEEP], out: "S"],
            [states: [INT_SLEEP, DEFUNCT], out: "SZ"],
        ]
        testCases.each {
            def expected = dep.formatFactory.create().format(it.states as Set)
            assert it.out == expected
        }
    }
}
