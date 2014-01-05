/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.bindings.BindingAddress
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.core.service.ServiceModule
import com.anrisoftware.sscontrol.mail.api.MailService
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see MailServiceImpl
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MailServiceTest {

    @Test
    void "service"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService mailService, profile
        def service = assertService registry.getService("mail")[0], tmpdir
        assert service.debug.level == 1
        assert service.resetDomains.resetDomains == true
        assert service.resetDomains.resetUsers == false
        assert service.resetDomains.resetAliases == false
    }

    @Test
    void "service mysql"() {
        loader.loadService ubuntu1004Profile, null
        def profile = registry.getService("profile")[0]
        loader.loadService mailMysqlService, profile
        def service = registry.getService("mail")[0]
        assert service.database.database == "maildb"
        assert service.database.user == "root"
        assert service.database.password == "password"
        assert service.database.server == "localhost"
        assert service.database.port == 663
    }

    static ubuntu1004Profile = MailServiceTest.class.getResource("Ubuntu_10_04Profile.groovy")

    static mailService = MailServiceTest.class.getResource("Mail.groovy")

    static mailMysqlService = MailServiceTest.class.getResource("MailMysqlService.groovy")

    static Injector injector

    static ServiceLoaderFactory loaderFactory

    static cert = MailServiceTest.class.getResource("cert_crt.txt")

    static key = MailServiceTest.class.getResource("cert_key.txt")

    static ca = MailServiceTest.class.getResource("cert_ca.txt")

    static pem = MailServiceTest.class.getResource("cert_pem.txt")

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    ServicesRegistry registry

    SscontrolServiceLoader loader

    Map variables

    File tmpdir

    File mail

    @Before
    void createTemp() {
        tmpdir = tmp.newFolder("mail-service")
        mail = new File(tmpdir, "/etc/mail")
        variables = [tmp: tmpdir.absoluteFile]
    }

    @Before
    void createRegistry() {
        registry = injector.getInstance ServicesRegistry
        loader = loaderFactory.create registry, variables
        loader.setParent injector
    }

    @BeforeClass
    static void createFactories() {
        injector = createInjector()
        loaderFactory = injector.getInstance ServiceLoaderFactory
    }

    static Injector createInjector() {
        Guice.createInjector(
                new CoreModule(), new CoreResourcesModule(), new ServiceModule())
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }

    static MailServiceImpl assertService(MailService service, File tmpdir) {
        assert service.binding.addresses.size() == 1
        assert service.binding.addresses[0].address == BindingAddress.all
        assert service.relayHost == "smtp.relayhost.com"
        assert service.domainName == "mail.example.com"
        assert service.origin == "example.com"
        assert service.masqueradeDomains.domains.contains("mail.example.com")
        assert service.masqueradeDomains.userExceptions.contains("root")
        assert service.destinations.contains("foo.bar")
        assert service.destinations.contains("bar.bar")
        assert service.certificate.cert == cert.toURI()
        assert service.certificate.key == key.toURI()
        assert service.certificate.ca == ca.toURI()
        assert service.certificate.pem == pem.toURI()
        assert service.domains.size() == 6
        assert service.domains[0].name == "example.com"
        assert service.domains[1].name == "mail.blobber.org"
        assert service.domains[1].aliases.size() == 1
        assert service.domains[1].aliases[0].name == ""
        assert service.domains[1].aliases[0].destination == "@blobber.org"
        assert service.domains[2].name == "blobber.org"
        assert service.domains[2].users.size() == 3
        service
    }
}
