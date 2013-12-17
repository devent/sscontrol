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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.wordpress

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ServiceConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.wordpress.BaseWordpress_3_Config
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * Configures the Wordpress service for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WordpressConfig extends BaseWordpress_3_Config implements ServiceConfig {

    @Inject
    private WordpressConfigLogger log

    @Inject
    private FcgiConfig fcgiConfig

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
    }

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    void deployDomain(Domain domain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.deployConfig domain
        createDomainConfig domain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.installPackages()
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        downloadArchive domain
        installPackages wordpressPackages
        enableMods wordpressMods
        createDomainConfig domain, service, config
        deployMainConfig service, domain
        deployDatabaseConfig service, domain
        deployKeysConfig service, domain
        deployLanguageConfig service, domain
        deploySecureLoginConfig service, domain
        deployDebugConfig service, domain
        deployMainConfigEnding service, domain
        createDirectories domain
        setupPermissions domain
    }

    void createDomainConfig(Domain domain, WebService service, List config) {
        def serviceAliasDir = serviceAliasDir service, domain
        def serviceDir = serviceDir domain
        def configStr = wordpressConfigTemplate.getText(
                true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "config", this,
                "serviceAliasDir", serviceAliasDir,
                "serviceDir", serviceDir)
        config << configStr
    }

    void downloadArchive(Domain domain) {
        def dir = wordpressDir domain
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-3-8-$name")
        def type = archiveType file: dest
        def strip = stripArchive
        if (!dir.isDirectory()) {
            dir.mkdirs()
        }
        copyURLToFile wordpressArchive.toURL(), dest
        unpack file: dest, type: type, output: dir, override: true, strip: strip
        log.downloadArchive script, wordpressArchive
    }

    void createDirectories(Domain domain) {
        wordpressContentCacheDir(domain).mkdirs()
        wordpressContentPluginsDir(domain).mkdirs()
        wordpressContentThemesDir(domain).mkdirs()
        wordpressContentUploadsDir(domain).mkdirs()
    }

    void setupPermissions(Domain domain) {
        def user = domain.domainUser
        script.changeOwner owner: "root", ownerGroup: user.group, files: [
            configurationFile(domain),
        ]
        script.changeMod mod: "0440", files: [
            configurationFile(domain),
        ]
        script.changeOwner owner: user.name, ownerGroup: user.group, files: [
            wordpressContentCacheDir(domain),
            wordpressContentPluginsDir(domain),
            wordpressContentThemesDir(domain),
            wordpressContentUploadsDir(domain),
        ]
        script.changeMod mod: "0775", files: [
            wordpressContentCacheDir(domain),
            wordpressContentPluginsDir(domain),
            wordpressContentThemesDir(domain),
            wordpressContentUploadsDir(domain),
        ]
    }

    /**
     * @see FcgiConfig#getScriptsSubdirectory()
     */
    String getScriptsSubdirectory() {
        fcgiConfig.scriptsSubdirectory
    }

    /**
     * @see FcgiConfig#getScriptStarterFileName()
     */
    String getScriptStarterFileName() {
        fcgiConfig.scriptStarterFileName
    }
}
