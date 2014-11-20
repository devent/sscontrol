/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04.WordpressResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuTestUtil

/**
 * Ubuntu 12.04 Wordpress.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class WordpressTest extends UbuntuTestUtil {

    @Test
    void "wordpress"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        basicWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent basicPortsConfExpected.asFile(tmpdir), basicPortsConfExpected
        assertFileContent basicDomainsConf.asFile(tmpdir), basicDomainsConf
        assertStringContent basicWwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comConfExpected.toString()
        assertStringContent basicWwwtest1comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comFcgiScriptExpected.toString()
        assertStringContent basicWwwtest1comPhpiniExpected.replaced(tmpdir, tmpdir, "/tmp"), basicWwwtest1comPhpiniExpected.toString()
        assertFileContent basicWordpressConfigExpected.asFile(tmpdir), basicWordpressConfigExpected
        assertFileContent basicAptitudeOutExpected.asFile(tmpdir), basicAptitudeOutExpected
        assertFileContent basicE2enmodOutExpected.asFile(tmpdir), basicE2enmodOutExpected
        assertStringContent basicChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChownOutExpected.toString()
        assertStringContent basicChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicChmodOutExpected.toString()
        assertStringContent basicGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicGroupaddOutExpected.toString()
        assertStringContent basicUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicUseraddOutExpected.toString()
        assertStringContent basicTarOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicTarOutExpected.toString()
        assert basicWwwtest1comWordpressCacheDir.asFile(tmpdir).isDirectory()
        assert basicWwwtest1comWordpressPluginsDir.asFile(tmpdir).isDirectory()
        assert basicWwwtest1comWordpressThemesDir.asFile(tmpdir).isDirectory()
        assert basicWwwtest1comWordpressUploadsDir.asFile(tmpdir).isDirectory()
    }

    @Test
    void "wordpress backup"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        basicWordpressConfigSample.createFile tmpdir
        test1WordpressDir.asFile tmpdir mkdirs()
        backupTarget.asFile tmpdir mkdirs()

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdBackupScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent backupTarOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{4}-\d{2}-\d{2}T\d{2}-\d{2}-\d{2}-\d{3}/, "000T00"), backupTarOutExpected.toString()
        assertStringContent mysqldumpOutExpected.replaced(tmpdir, tmpdir, "/tmp").replaceAll(/\d{4}-\d{2}-\d{2}T\d{2}-\d{2}-\d{2}-\d{3}/, "000T00"), mysqldumpOutExpected.toString()
    }

    @Test
    void "wordpress ref"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        refWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdRefScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent refPortsConfExpected.asFile(tmpdir), refPortsConfExpected
        assertFileContent refDomainsConf.asFile(tmpdir), refDomainsConf
        assertStringContent refWwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), refWwwtest1comConfExpected.toString()
        assertStringContent refWwwtest1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), refWwwtest1comSslConfExpected.toString()
        assertStringContent refWwwtest1comFcgiScriptExpected.replaced(tmpdir, tmpdir, "/tmp"), refWwwtest1comFcgiScriptExpected.toString()
        assertFileContent refWordpressConfigExpected.asFile(tmpdir), refWordpressConfigExpected
        assertStringContent refChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), refChownOutExpected.toString()
        assertStringContent refChmodRetExpected.replaced(tmpdir, tmpdir, "/tmp"), refChmodRetExpected.toString()
    }

    @Test
    void "wordpress de_DE"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        basicdeWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        profile.getEntry("httpd").wordpress_language Locale.GERMANY
        loader.loadService httpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent basicdeWordpressConfigExpected.asFile(tmpdir), basicdeWordpressConfigExpected
        assertStringContent basicdeTarOutExpected.replaced(tmpdir, tmpdir, "/tmp"), basicdeTarOutExpected.toString()
    }

    @Test
    void "wordpress debug"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        debugWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdDebugScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent debugWordpressConfigExpected.asFile(tmpdir), debugWordpressConfigExpected
    }

    @Test
    void "wordpress root alias"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        rootWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdRootScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent rootWwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), rootWwwtest1comConfExpected.toString()
    }

    @Test
    void "wordpress ms subdomain"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        subdomainWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdMsSubdomainScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent subdomainWwwtest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainWwwtest1comConfExpected.toString()
        assertStringContent subdomainWwwblogfoocomConfExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainWwwblogfoocomConfExpected.toString()
        assertStringContent subdomainWwwblogbarcomConfExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainWwwblogbarcomConfExpected.toString()
        assertStringContent subdomainChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainChownOutExpected.toString()
        assertStringContent subdomainChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainChmodOutExpected.toString()
        assertStringContent subdomainGroupaddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainGroupaddOutExpected.toString()
        assertStringContent subdomainUseraddOutExpected.replaced(tmpdir, tmpdir, "/tmp"), subdomainUseraddOutExpected.toString()
        assertFileContent subdomainWordpressConfigExpected.asFile(tmpdir), subdomainWordpressConfigExpected
    }

    @Test
    void "wordpress themes, plugins"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        plguinsWordpressConfigSample.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdPluginsScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertStringContent pluginsUnzipOutExpected.replaced(tmpdir, tmpdir, "/tmp"), pluginsUnzipOutExpected.toString()
    }

    @Test
    void "wordpress prefix, no override"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        nooverrideWordpressConfig.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScriptNoOverride.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent nooverrideWwwTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), nooverrideWwwTest1comConfExpected.toString()
        assertStringContent nooverrideWwwTest1comSslConfExpected.replaced(tmpdir, tmpdir, "/tmp"), nooverrideWwwTest1comSslConfExpected.toString()
        assertFileContent nooverrideWordpressConfigExpected.asFile(tmpdir), nooverrideWordpressConfigExpected
        assertStringContent nooverrideChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), nooverrideChownOutExpected.toString()
        assertStringContent nooverrideChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), nooverrideChmodOutExpected.toString()
        assert nooverrideTarOutExpected.asFile(tmpdir).isFile() == false
    }

    @Test
    void "wordpress hyper-cache"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir
        hypercacheWordpressConfig.createFile tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService hypercacheHttpdScript.resource, profile, preScript

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertStringContent hypercacheWwwTest1comConfExpected.replaced(tmpdir, tmpdir, "/tmp"), hypercacheWwwTest1comConfExpected.toString()
        assertFileContent hypercacheWordpressConfigExpected.asFile(tmpdir), hypercacheWordpressConfigExpected
        assertStringContent hypercacheUnzipOutExpected.replaced(tmpdir, tmpdir, "/tmp"), hypercacheUnzipOutExpected.toString()
        assertStringContent hypercacheChownOutExpected.replaced(tmpdir, tmpdir, "/tmp"), hypercacheChownOutExpected.toString()
        assertStringContent hypercacheChmodOutExpected.replaced(tmpdir, tmpdir, "/tmp"), hypercacheChmodOutExpected.toString()
    }
}
