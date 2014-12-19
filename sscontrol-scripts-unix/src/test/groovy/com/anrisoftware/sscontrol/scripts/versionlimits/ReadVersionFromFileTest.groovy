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
package com.anrisoftware.sscontrol.scripts.versionlimits

import org.apache.commons.io.Charsets
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.globalpom.version.VersionModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see ReadVersion
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ReadVersionFromFileTest {

    @Test
    void "read version resource"() {
        def readVersion = factory.create(versionFile, Charsets.UTF_8)
        def version = readVersion.readVersion()
        assert version.major == 1
        assert version.minor == 2
        assert version.revision == 3
    }

    @Test
    void "read version resource, empty file"() {
        def readVersion = factory.create(versionEmptyFile, Charsets.UTF_8)
        def version = readVersion.readVersion()
        assert version == null
    }

    static Injector injector

    static ReadVersionFactory factory

    static URI versionFile = ReadVersionFromFileTest.class.getResource("version_file.txt").toURI()

    static URI versionEmptyFile = ReadVersionFromFileTest.class.getResource("version_empty_file.txt").toURI()

    @BeforeClass
    static void createFactory() {
        TestUtils.toStringStyle
        this.injector = Guice.createInjector(new VersionModule(), new VersionLimitsModule())
        this.factory = injector.getInstance ReadVersionFactory
    }
}
