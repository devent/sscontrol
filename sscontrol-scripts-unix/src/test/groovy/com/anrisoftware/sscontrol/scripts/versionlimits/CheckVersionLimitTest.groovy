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
package com.anrisoftware.sscontrol.scripts.versionlimits

import groovy.util.logging.Slf4j

import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.globalpom.version.VersionModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see CheckVersionLimit
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class CheckVersionLimitTest {

    @Test
    void "check versions"() {
        def cases = [
            [currentVersion: "0.8.0", archiveVersion: "0.8.1", versionLimit: "1.0.0", expected: true],
            [currentVersion: "0.9.0", archiveVersion: "0.9.0", versionLimit: "1.0.0", expected: true],
            [currentVersion: "1.0.0", archiveVersion: "0.9.0", versionLimit: "1.0.0", expected: false],
            [currentVersion: "0.8.0", archiveVersion: "1.5.0", versionLimit: "1.0.0", expected: false],
        ]
        def format = versionFormatFactory.create()
        cases.each {
            def currentVersion = format.parse(it.currentVersion)
            def archiveVersion = format.parse(it.archiveVersion)
            def versionLimit = format.parse(it.versionLimit)
            def checkVersion = factory.create(currentVersion, archiveVersion, versionLimit)
            log.info "Check version {}<={}&{}<={} := {}", it.currentVersion, it.archiveVersion, it.archiveVersion, it.versionLimit, it.expected
            assert checkVersion.checkVersion() == it.expected
        }
    }

    static Injector injector

    static CheckVersionLimitFactory factory

    static VersionFormatFactory versionFormatFactory

    @BeforeClass
    static void createFactory() {
        TestUtils.toStringStyle
        this.injector = Guice.createInjector(new VersionModule(), new VersionLimitsModule())
        this.factory = injector.getInstance CheckVersionLimitFactory
        this.versionFormatFactory = injector.getInstance VersionFormatFactory
    }
}
