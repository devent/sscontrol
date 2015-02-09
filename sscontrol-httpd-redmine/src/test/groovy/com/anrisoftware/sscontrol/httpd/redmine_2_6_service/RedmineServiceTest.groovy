/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine_2_6_service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.redmine.ubuntu_12_04_nginx_thin_redmine_2_6.RedmineResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.AuthenticationMethod
import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.ScmInstall
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.testutils.resources.HttpdTestEnvironment;

/**
 * @see RedmineService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedmineServiceTest extends HttpdTestEnvironment {

    @Test
    void "redmine"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        RedmineService webservice = domain.services[0]
        assert webservice.name == "redmine_2_6"
        assert webservice.id == "redmineid"
        assert webservice.backend == "thin"
        assert webservice.ref == null
        assert webservice.alias == null
        assert webservice.prefix == null
        assert webservice.ref == null
        assert webservice.refDomain == null
        assert webservice.debugLogging("level")["thin"] == 1
        assert webservice.debugLogging("level")["redmine"] == 1
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "redmine2"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.mail.host == "smtp.test1.com"
        assert webservice.mail.port == 25
        assert webservice.mail.method == DeliveryMethod.smtp
        assert webservice.mail.domain == "example.net"
        assert webservice.mail.auth == AuthenticationMethod.login
        assert webservice.mail.user == "redmine@example.net"
        assert webservice.mail.password == "redminepass"
        assert webservice.languageName == "de"
        assert webservice.scms.containsAll([
            ScmInstall.subversion,
            ScmInstall.mercurial
        ])

        domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        webservice = domain.services[0]
        assert webservice.name == "redmine_2_6"
        assert webservice.id == null
        assert webservice.backend == "thin"
        assert webservice.alias == null
        assert webservice.prefix == null
        assert webservice.ref == "redmineid"
        assert webservice.refDomain == null
        assert webservice.debugLogging("level") == null
        assert webservice.overrideMode == null
        assert webservice.database == null
        assert webservice.mail == null
        assert webservice.languageName == null
        assert webservice.scms == null

        domain = service.domains[d++]
        assert domain.name == "test2.com"
        assert domain.address == "192.168.0.52"

        webservice = domain.services[0]
        assert webservice.name == "redmine_2_6"
        assert webservice.id == "test2comRedmineid"
        assert webservice.backend == "thin"
        assert webservice.alias == "projects"
        assert webservice.prefix == "test2redmine"
        assert webservice.ref == null
        assert webservice.refDomain == null
        assert webservice.debugLogging("level") == null
        assert webservice.overrideMode == null
        assert webservice.database.database == "redmine2"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.mail.host == null
        assert webservice.mail.port == null
        assert webservice.mail.method == null
        assert webservice.mail.domain == null
        assert webservice.mail.auth == null
        assert webservice.mail.user == "redmine@example.net"
        assert webservice.mail.password == "redminepass"
        assert webservice.languageName == null
        assert webservice.scms == null
    }
}
