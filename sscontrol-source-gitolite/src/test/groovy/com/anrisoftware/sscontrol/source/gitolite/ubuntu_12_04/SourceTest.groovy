/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.source.gitolite.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.source.gitolite.test.PreScriptTestEnvironment;

/**
 * <i>Ubuntu 12.04 Gitolite</i> security script test.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SourceTest extends PreScriptTestEnvironment {

    @Test
    void "gitolite script"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntuProperties profile, tmpdir
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService sourceService.resource, profile, preScript
        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent aptitudeOutExpected.asFile(tmpdir), aptitudeOutExpected
        assertStringContent gitoliteInstallOutExpected.replaced(tmpdir, tmpdir, "/tmp"), gitoliteInstallOutExpected.toString()
        assertStringContent gitoliteOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{2,}/, "xx"), gitoliteOutExpected.toString()
    }
}
