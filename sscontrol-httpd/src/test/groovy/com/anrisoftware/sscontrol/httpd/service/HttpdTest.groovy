/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.auth.RequireValidGroup;
import com.anrisoftware.sscontrol.httpd.auth.RequireValidUser;
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType;
import com.anrisoftware.sscontrol.httpd.authldap.AttributeDn;
import com.anrisoftware.sscontrol.httpd.authldap.Authoritative;
import com.anrisoftware.sscontrol.httpd.authldap.RequireLdapValidGroup;

/**
 * @see HttpdServiceImpl
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdTest extends HttpdTestUtil {

    @Test
    void "httpd script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]

        assert service.domains.size() == 5
        assert service.virtualDomains.size() == 4
        assert service.debug.level == 1

        def domain = service.domains[0]
        assert domain.memory.limit.value == 32000000
        assert domain.memory.upload.value == 32000000
        assert domain.redirects.size() == 1
        assert domain.redirects[0].destination == "www.test1.com"
    }

    @Test
    void "httpd auth file"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService authFileScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]
        service.domains.size() == 2

        def auth = service.domains[0].auths[0]
        assert auth.name == "Private Directory"
        assert auth.location == "/private"
        assert auth.type == AuthType.digest
        assert auth.provider == AuthProvider.file
        assert auth.appending == true
        assert auth.satisfy == SatisfyType.any

        def domains = auth.domains
        assert domains.size() == 2
        assert domains[0] == "/private"
        assert domains[1] == "https://private"

        def require = auth.requires[0]
        assert require.class == RequireValidUser

        require = auth.requires[1]
        assert require.class == RequireValidGroup
        assert require.name == "admin"

        assert auth.groups.size() == 1
        def group = auth.groups[0]
        assert group.name == "admin"
        assert group.users.size() == 2

        def user = group.users[0]
        assert user.name == "adminfoo"
        assert user.password == "adminfoopassword"
        assert user.group == group

        user = group.users[1]
        assert user.name == "adminbar"
        assert user.password == "adminbarpassword"
        assert user.group == group

        user = auth.users[0]
        assert user.name == "bar"
        assert user.password == "barpassword"
    }

    @Test
    void "httpd auth ldap"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService authLdapScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]
        service.domains.size() == 1

        def auth = service.domains[0].auths[0]
        assert auth.name == "Private Directory"
        assert auth.location == "private"
        assert auth.type == AuthType.digest
        assert auth.provider == AuthProvider.ldap
        assert auth.satisfy == SatisfyType.any
        assert auth.authoritative == Authoritative.off

        def require = auth.requires[0]
        assert require.class == RequireValidUser

        require = auth.requires[1]
        assert require.class == RequireLdapValidGroup
        assert require.name == "cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com"
        assert require.attribute.name == "uniqueMember"
        assert require.attribute.dn == AttributeDn.off
    }
}
