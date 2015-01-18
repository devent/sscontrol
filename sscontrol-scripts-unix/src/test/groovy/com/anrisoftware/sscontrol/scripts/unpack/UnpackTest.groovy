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
package com.anrisoftware.sscontrol.scripts.unpack

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.unpack.Ubuntu_10_04_Resources.*
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
 * @see Unpack
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UnpackTest {

    @Test
    void "unpack targz"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        unpackFactory.create(
                log: log,
                file: targzFile.asFile(tmpdir),
                output: outputDir.asFile(tmpdir),
                commands: [tgz: targzCommand.asFile(tmpdir), zip: unzipCommand.asFile(tmpdir)],
                this, threads)()
        assertStringContent targzOutExpected.replaced(tmpdir, tmpdir, "/tmp"), targzOutExpected.toString()
    }

    @Test
    void "unpack targz override strip"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        unpackFactory.create(
                log: log,
                file: targzFile.asFile(tmpdir),
                output: outputDir.asFile(tmpdir),
                commands: [tgz: targzCommand.asFile(tmpdir), zip: unzipCommand.asFile(tmpdir)],
                override: true,
                strip: true,
                this, threads)()
        assertStringContent targzStripOutExpected.replaced(tmpdir, tmpdir, "/tmp"), targzStripOutExpected.toString()
    }

    @Test
    void "unpack zip"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        unpackFactory.create(
                log: log,
                file: zipFile.asFile(tmpdir),
                output: outputDir.asFile(tmpdir),
                commands: [tgz: targzCommand.asFile(tmpdir), zip: unzipCommand.asFile(tmpdir)],
                this, threads)()
        assertStringContent unzipOutExpected.replaced(tmpdir, tmpdir, "/tmp"), unzipOutExpected.toString()
    }

    @Test
    void "unpack zip override strip"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_10_04_Files tmpdir
        unpackFactory.create(
                log: log,
                file: zipFile.asFile(tmpdir),
                output: outputDir.asFile(tmpdir),
                commands: [tgz: targzCommand.asFile(tmpdir), zip: unzipCommand.asFile(tmpdir)],
                bashCommand: bashCommand.asFile(tmpdir),
                override: true,
                strip: true,
                this, threads)()
        assertStringContent bashOverrideStripOutExpected.replaced(tmpdir, tmpdir, "/tmp"), bashOverrideStripOutExpected.toString()
    }

    static Injector injector

    static UnpackFactory unpackFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new UnpackModule(),
                new UnixScriptsModule(),
                new UnixScriptsModule.UnixScriptsDefaultsModule(),
                new UnixScriptsModule.TemplatesResourcesDefaultsModule(),
                new PropertiesThreadsModule())
        unpackFactory = injector.getInstance UnpackFactory
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
