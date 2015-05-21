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
package com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.UbuntuResources.*
import groovy.util.logging.Slf4j

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * @see Ubuntu_12_04_InstallLocale
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 *
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_InstallLocaleTest extends UbuntuDependencies {

    @Test
    void "install locale"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_12_04_Files tmpdir
        def info = dep.installLocaleFactory.create(
                log: log,
                locales: ["de_DE.UTF-8"],
                localesDirectory: localesDir.asFile(tmpdir),
                installCommand: installCommand.asFile(tmpdir),
                dpkgReconfigureCommand: dpkgreconfigureCommand.asFile(tmpdir),
                system: "ubuntu",
                charset: Charset.defaultCharset(),
                this,
                dep.threads)
        info()
        assertFileContent dpkgreconfigureOutExpected.asFile(tmpdir), dpkgreconfigureOutExpected
        assertFileContent installOutExpected.asFile(tmpdir), installOutExpected
        assertStringContent deFileExpected.replaced(tmpdir, tmpdir, "/tmp"), deFileExpected.toString()
    }

    @Test
    void "install locale de"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_12_04_Files tmpdir
        alreadysetDeFile.createFile tmpdir
        def info = dep.installLocaleFactory.create(
                log: log,
                locales: ["de_DE.ISO-8859-1"],
                localesDirectory: localesDir.asFile(tmpdir),
                installCommand: installCommand.asFile(tmpdir),
                dpkgReconfigureCommand: dpkgreconfigureCommand.asFile(tmpdir),
                system: "ubuntu",
                charset: Charset.defaultCharset(),
                this,
                dep.threads)
        info()
        assertFileContent dpkgreconfigureOutExpected.asFile(tmpdir), dpkgreconfigureOutExpected
        assertFileContent installOutExpected.asFile(tmpdir), installOutExpected
        assertStringContent alreadysetDeFileExpected.replaced(tmpdir, tmpdir, "/tmp"), alreadysetDeFileExpected.toString()
    }

    @Test
    void "install locale de duplicate"() {
        def tmpdir = tmp.newFolder()
        copyUbuntu_12_04_Files tmpdir
        duplicateDeFile.createFile tmpdir
        def info = dep.installLocaleFactory.create(
                log: log,
                locales: ["de_DE.ISO-8859-1"],
                localesDirectory: localesDir.asFile(tmpdir),
                installCommand: installCommand.asFile(tmpdir),
                dpkgReconfigureCommand: dpkgreconfigureCommand.asFile(tmpdir),
                system: "ubuntu",
                charset: Charset.defaultCharset(),
                this,
                dep.threads)
        info()
        assertFileContent dpkgreconfigureOutExpected.asFile(tmpdir), dpkgreconfigureOutExpected
        assertFileContent installOutExpected.asFile(tmpdir), installOutExpected
        assertStringContent duplicateDeFileExpected.replaced(tmpdir, tmpdir, "/tmp"), duplicateDeFileExpected.toString()
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()
}
