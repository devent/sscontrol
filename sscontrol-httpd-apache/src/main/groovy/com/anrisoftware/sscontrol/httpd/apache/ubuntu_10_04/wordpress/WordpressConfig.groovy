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
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService
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
    void deployService(Domain domain, WebService service, List serviceConfig) {
        fcgiConfig.script = script
        installPackages wordpressPackages
        downloadArchive()
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        deployMainConfig service
        deployDatabaseConfig service
        deployKeysConfig service
        deployLanguageConfig service
        deploySecureLoginConfig service
        deployDebugConfig service
        deployMainConfigEnding service
    }

    void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
        def config = wordpressConfigTemplate.getText(true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "fcgiProperties", fcgiConfig)
        serviceConfig << config
    }

    void downloadArchive() {
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-$name")
        def type = archiveType file: dest
        if (!wordpressDir.isDirectory()) {
            wordpressDir.mkdirs()
        }
        copyURLToFile wordpressArchive.toURL(), dest
        unpack file: dest, type: type, output: wordpressDir, override: true
        log.downloadArchive script, wordpressArchive
    }

    def changeOwnerConfiguration(Domain domain) {
        def user = domain.domainUser
        changeOwner owner: "root", ownerGroup: user.group, files: [
            localBlowfishFile,
            localConfigFile,
            localDatabaseConfigFile
        ]
    }
}
