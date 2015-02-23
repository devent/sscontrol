/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.auth.RequireGroup
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireUser
import com.anrisoftware.sscontrol.httpd.auth.RequireValid
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType
import com.anrisoftware.sscontrol.httpd.authdb.AuthDbService
import com.anrisoftware.sscontrol.httpd.authfile.AuthFileService
import com.anrisoftware.sscontrol.httpd.authfile.RequireDomain
import com.anrisoftware.sscontrol.httpd.authldap.AuthLdapService
import com.anrisoftware.sscontrol.testutils.resources.HttpdTestEnvironment

/**
 * @see HttpdService
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdTest extends HttpdTestEnvironment {

    @Test
    void "httpd script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdScript.resource, profile
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 5
        assert service.virtualDomains.size() == 4
        assert service.debugLogging("level").size() == 1
        assert service.debugLogging("level")["error"] == 1

        def domain = service.domains[0]
        assert domain.memory.limit.value == 32000000
        assert domain.memory.upload.value == 32000000
        assert domain.debug.php == 1
        assert domain.redirects.size() == 1
        assert domain.redirects[0].destination == "www.test1.com"
        assert domain.errorPage == "/50x.html"
        assert domain.errorCodes.size() == 4
        assert domain.errorCodes.containsAll(["500", "502", "503", "504"])
        assert domain.errorRoot == "/usr/share/nginx/html"

        domain = service.domains[2]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.50"
        assert domain.redirects.size() == 1
        assert domain.redirects[0].destination == "www.test1.com"
        assert domain.certResource.toString().endsWith("cert_crt.txt")
        assert domain.keyResource.toString().endsWith("cert_key.txt")
        assert domain.caResource == null
        assert domain.errorPage == null
        assert domain.errorCodes == null
        assert domain.errorRoot == null

        domain = service.domains[4]
        assert domain.name == "test2.com"
        assert domain.address == "192.168.0.51"
        assert domain.redirects.size() == 1
        assert domain.redirects[0].destination == "www.test2.com"
        assert domain.certResource.toString().endsWith("cert_crt.txt")
        assert domain.keyResource.toString().endsWith("cert_key.txt")
        assert domain.caResource.toString().endsWith("cert_ca.txt")
    }

    @Test
    void "httpd auth file"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService authFileScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1

        AuthFileService auth = service.domains[0].services[0]
        assert auth.name == "auth-file"
        assert auth.auth == "Private Directory"
        assert auth.location == "/private"
        assert auth.type == AuthType.digest
        assert auth.satisfy == SatisfyType.any

        assert auth.requireValids.size() == 1
        assert auth.requireValids.containsAll([RequireValid.user])

        assert auth.requireDomains.size() == 1
        RequireDomain domain = auth.requireDomains[0]
        assert domain.domain == "https://test1.com"

        assert auth.requireUsers.size() == 2
        RequireUser user = auth.requireUsers[0]
        assert user.name == "foo"
        assert user.password == "foopassword"
        assert user.updateMode == null

        user = auth.requireUsers[1]
        assert user.name == "bar"
        assert user.password == "barpassword"
        assert user.updateMode == RequireUpdate.password

        assert auth.requireGroups.size() == 4
        RequireGroup group = auth.requireGroups[0]
        assert group.name == "foogroup"
        assert group.users.size() == 0

        group = auth.requireGroups[1]
        assert group.name == "admin1"
        assert group.users.size() == 2

        group = auth.requireGroups[2]
        assert group.name == "admin2"
        assert group.updateMode == RequireUpdate.rewrite
        assert group.users.size() == 2

        group = auth.requireGroups[3]
        assert group.name == "admin3"
        assert group.updateMode == RequireUpdate.append
        assert group.users.size() == 2
    }

    @Test
    void "httpd auth ldap"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService authLdapScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1

        AuthLdapService auth = service.domains[0].services[0]
        assert auth.name == "auth-ldap"
        assert auth.auth == "Private Directory"
        assert auth.location == "/private"
        assert auth.type == AuthType.basic
        assert auth.satisfy == SatisfyType.any
        assert auth.authoritative == false
        assert auth.host == "ldap://127.0.0.1:389"
        assert auth.hostUrl == "o=deventorg,dc=ubuntutest,dc=com?cn"
        assert auth.credentials == "cn=admin,dc=ubuntutest,dc=com"
        assert auth.credentialsPassword == "adminpass"

        assert auth.requireValids.size() == 1
        assert auth.requireValids.containsAll([RequireValid.user])

        assert auth.requireGroups.size() == 1
        RequireGroup group = auth.requireGroups[0]
        assert group.name == "cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com"

        def attr = auth.attributes
        assert attr.size() == 2
        assert attr.group == "uniqueMember"
        assert attr.dn == false
    }

    @Test
    void "httpd auth db"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService authDbScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1

        AuthDbService auth = service.domains[0].services[0]
        assert auth.name == "auth-db"
        assert auth.auth == "Private Directory"
        assert auth.location == "/private"
        assert auth.database.database == "authdb"
        assert auth.database.user == "userdb"
        assert auth.database.password == "userpassdb"
        assert auth.database.host == "localhost"
        assert auth.database.driver == "mysql"
        assert auth.database.encryption.size() == 2
        assert auth.database.encryption.containsAll(["PHP_MD5", "Crypt"])
        assert auth.usersTable == "users"
        assert auth.userNameField == "username"
        assert auth.passwordField == "passwd"
        assert auth.allowEmptyPasswords == false

        assert auth.requireValids.size() == 1
        assert auth.requireValids.containsAll([RequireValid.user])

        RequireUser user = auth.requireUsers[0]
        assert user.name == "foo"
        assert user.password == "foopassword"
        assert user.updateMode == null

        user = auth.requireUsers[1]
        assert user.name == "bar"
        assert user.password == "barpassword"
        assert user.updateMode == RequireUpdate.password

        assert auth.requireGroups.size() == 4
        RequireGroup group = auth.requireGroups[0]
        assert group.name == "foogroup"
        assert group.users.size() == 0

        group = auth.requireGroups[1]
        assert group.name == "admin1"
        assert group.users.size() == 2

        group = auth.requireGroups[2]
        assert group.name == "admin2"
        assert group.updateMode == RequireUpdate.rewrite
        assert group.users.size() == 2

        group = auth.requireGroups[3]
        assert group.name == "admin3"
        assert group.updateMode == RequireUpdate.append
        assert group.users.size() == 2
    }
}
