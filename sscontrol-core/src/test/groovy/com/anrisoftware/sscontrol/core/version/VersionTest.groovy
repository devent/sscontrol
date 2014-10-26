/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.version

import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see Version
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class VersionTest {

    @Test
    void "compare versions"() {
        testCases.each {
            def lhs = factory.create it.lhs.major, it.lhs.minor, it.lhs.rev
            def rhs = factory.create it.rhs.major, it.rhs.minor, it.rhs.rev
            def expected = lhs.compareTo(rhs)
            log.info "Compare {} and {} => {}", lhs, rhs, expected
            assert expected == it.compared
        }
    }

    static testCases

    static Injector injector

    static VersionFactory factory

    @BeforeClass
    static void createFactory() {
        TestUtils.toStringStyle
        this.injector = Guice.createInjector(new VersionModule())
        this.factory = injector.getInstance VersionFactory
        this.testCases = [
            [lhs: [major: 1, minor: 0, rev: 0], rhs: [major: 1, minor: 0, rev: 0], compared: 0],
            [lhs: [major: 2, minor: 0, rev: 0], rhs: [major: 1, minor: 0, rev: 0], compared: 1],
            [lhs: [major: 1, minor: 5, rev: 0], rhs: [major: 1, minor: 0, rev: 0], compared: 1],
            [lhs: [major: 1, minor: 0, rev: 1], rhs: [major: 1, minor: 0, rev: 0], compared: 1],
            [lhs: [major: 1, minor: 10, rev: 1], rhs: [major: 1, minor: 12, rev: 3], compared: -1],
            [lhs: [major: 1, minor: 10, rev: 1], rhs: [major: 1, minor: 10, rev: 3], compared: -1],
        ]
    }
}
