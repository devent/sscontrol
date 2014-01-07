/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_12_04.Ubuntu_12_04_Resources.*
import static com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04.WordpressResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Ubuntu 10.04 Wordpress.
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

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent portsConfExpected.asFile(tmpdir), portsConfExpected
        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), domainsConf
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertStringContent wwwtest1comConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comConf.toString()
        assertStringContent wwwtest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslConf.toString()
        assertStringContent wwwtest1comSslFcgiScript.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslFcgiScript.toString()
        assertFileContent wordpress_3_8_config_expected.asFile(tmpdir), wordpress_3_8_config_expected
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertStringContent tarOut.replaced(tmpdir, tmpdir, "/tmp"), tarOut.toString()
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent a2enmodOut.asFile(tmpdir), a2enmodOut
        assert wordpress_3_8_cache_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_plugins_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_themes_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_uploads_dir.asFile(tmpdir).isDirectory()
    }

    @Test
    void "wordpress ref"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdRefScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), domainsConf
        assertStringContent test1comRefConf.replaced(tmpdir, tmpdir, "/tmp"), test1comRefConf.toString()
        assertStringContent test1comSslRefConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslRefConf.toString()
        assertStringContent wwwtest1comRefConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comRefConf.toString()
        assertStringContent wwwtest1comSslRefConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslRefConf.toString()
        assertStringContent wwwtest1comSslFcgiScript.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslFcgiScript.toString()
        assertFileContent wordpress_3_8_config_expected.asFile(tmpdir), wordpress_3_8_config_expected
        assertStringContent chownRetOut.replaced(tmpdir, tmpdir, "/tmp"), chownRetOut.toString()
        assertStringContent chmodRetOut.replaced(tmpdir, tmpdir, "/tmp"), chmodRetOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertStringContent tarOut.replaced(tmpdir, tmpdir, "/tmp"), tarOut.toString()
        assertFileContent aptitudeOut.asFile(tmpdir), aptitudeOut
        assertFileContent a2enmodOut.asFile(tmpdir), a2enmodOut
        assert wordpress_3_8_cache_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_plugins_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_themes_dir.asFile(tmpdir).isDirectory()
        assert wordpress_3_8_uploads_dir.asFile(tmpdir).isDirectory()
    }

    @Test
    void "wordpress de_DE"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        profile.getEntry("httpd").wordpress_language Locale.GERMANY
        loader.loadService httpdScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), domainsConf
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertFileContent wordpress_3_8_de_DE_config_expected.asFile(tmpdir), wordpress_3_8_de_DE_config_expected
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertStringContent tar_de_DE_Out.replaced(tmpdir, tmpdir, "/tmp"), tar_de_DE_Out.toString()
    }

    @Test
    void "wordpress debug"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdDebugScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertFileContent defaultConf.asFile(tmpdir), defaultConf
        assertFileContent domainsConf.asFile(tmpdir), domainsConf
        assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
        assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
        assertFileContent wordpress_3_8_debug_config_expected.asFile(tmpdir), wordpress_3_8_debug_config_expected
        assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
        assertStringContent chmodOut.replaced(tmpdir, tmpdir, "/tmp"), chmodOut.toString()
        assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
        assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
        assertStringContent tarOut.replaced(tmpdir, tmpdir, "/tmp"), tarOut.toString()
    }

    @Test
    void "wordpress root alias"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdRootScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent wwwtest1comRootConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comRootConf.toString()
        assertStringContent wwwtest1comSslRootConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslRootConf.toString()
    }

    @Test
    void "wordpress ms subdirectory"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdMsSubdirectoryScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }

        assertStringContent wwwtest1comMsSubdirectoryConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comMsSubdirectoryConf.toString()
        assertStringContent wwwtest1comSslMsSubdirectoryConf.replaced(tmpdir, tmpdir, "/tmp"), wwwtest1comSslMsSubdirectoryConf.toString()
        assertStringContent wwwblogfoocomMsSubdirectoryConf.replaced(tmpdir, tmpdir, "/tmp"), wwwblogfoocomMsSubdirectoryConf.toString()
        assertStringContent wwwblogbarcomMsSubdirectoryConf.replaced(tmpdir, tmpdir, "/tmp"), wwwblogbarcomMsSubdirectoryConf.toString()
        assertStringContent chownMsSubdirectoryOut.replaced(tmpdir, tmpdir, "/tmp"), chownMsSubdirectoryOut.toString()
        assertStringContent chmodMsSubdirectoryOut.replaced(tmpdir, tmpdir, "/tmp"), chmodMsSubdirectoryOut.toString()
        assertStringContent groupaddMsSubdirectoryOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddMsSubdirectoryOut.toString()
        assertStringContent useraddMsSubdirectoryOut.replaced(tmpdir, tmpdir, "/tmp"), useraddMsSubdirectoryOut.toString()
        assertFileContent wordpress_3_8_MsSubdirectoryConfigExpected.asFile(tmpdir), wordpress_3_8_MsSubdirectoryConfigExpected
    }

    @Test
    void "wordpress themes, plugins"() {
        copyUbuntuFiles tmpdir
        copyUbuntu_12_04_Files tmpdir
        copyWordpressFiles tmpdir

        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        setupUbuntu_12_04_Properties profile, tmpdir
        loader.loadService httpdPluginsScript.resource, profile

        registry.allServices.each { it.call() }
        log.info "Run service again to ensure that configuration is not set double."
        registry.allServices.each { it.call() }
        assertStringContent unzipPluginsOut.replaced(tmpdir, tmpdir, "/tmp"), unzipPluginsOut.toString()
    }
}
