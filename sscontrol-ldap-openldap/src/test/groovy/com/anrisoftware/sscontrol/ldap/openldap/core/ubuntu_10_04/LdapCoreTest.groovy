/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap-openldap.
 *
 * sscontrol-ldap-openldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap-openldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap-openldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.core.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.ldap.openldap.ubuntu.UbuntuTestUtil

/**
 * Test OpenLDAP on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LdapCoreTest extends UbuntuTestUtil {

    @Test
    void "ldap core"() {
        UbuntuResources.copyUbuntuFiles tmpdir
        loader.loadService UbuntuResources.profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService LdapCoreResources.ldapScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent LdapCoreResources.dbConf.asFile(tmpdir), LdapCoreResources.dbConf
        assertFileContent LdapCoreResources.baseConf.asFile(tmpdir), LdapCoreResources.baseConf
        assertFileContent LdapCoreResources.systemConf.asFile(tmpdir), LdapCoreResources.systemConf
        assertFileContent LdapCoreResources.ldapConf.asFile(tmpdir), LdapCoreResources.ldapConf
        assertStringContent LdapCoreResources.chmodOut.replaced(tmpdir, tmpdir, "/tmp"), LdapCoreResources.chmodOut.toString()
        assertStringContent LdapCoreResources.ldapaddOut.replaced(tmpdir, tmpdir, "/tmp"), LdapCoreResources.ldapaddOut.toString()
        assertStringContent LdapCoreResources.ldapmodifyOut.replaced(tmpdir, tmpdir, "/tmp"), LdapCoreResources.ldapmodifyOut.toString()
        assertFileContent LdapCoreResources.restartOut.asFile(tmpdir), LdapCoreResources.restartOut
    }
}
