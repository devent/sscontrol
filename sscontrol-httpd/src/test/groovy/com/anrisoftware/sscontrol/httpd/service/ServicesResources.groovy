/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum ServicesResources {

    profile("UbuntuProfile.groovy", ServicesResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", ServicesResources.class.getResource("Httpd.groovy")),
    httpdPortsScript("Httpd.groovy", ServicesResources.class.getResource("HttpdPorts.groovy")),
    authFileScript("Httpd.groovy", ServicesResources.class.getResource("HttpdAuthFile.groovy")),
    authLdapScript("Httpd.groovy", ServicesResources.class.getResource("HttpdAuthLdap.groovy")),
    phpldapadminScript("Httpd.groovy", ServicesResources.class.getResource("HttpdPhpldapadmin.groovy")),
    roundcubeScript("Httpd.groovy", ServicesResources.class.getResource("HttpdRoundcube.groovy")),
    wordpressScript("Httpd.groovy", ServicesResources.class.getResource("HttpdWordpress.groovy")),
    proxyScript("Httpd.groovy", ServicesResources.class.getResource("HttpdProxy.groovy")),
    proxyDomainsScript("Httpd.groovy", ServicesResources.class.getResource("HttpdProxyDomains.groovy")),
    httpdUserMapScript("Httpd.groovy", ServicesResources.class.getResource("HttpdUserMap.groovy")),
    httpdUserRefDomainScript("Httpd.groovy", ServicesResources.class.getResource("HttpdUserRefDomain.groovy")),
    gititScript("Httpd.groovy", ServicesResources.class.getResource("HttpdGitit.groovy")),

    ResourcesUtils resources

    ServicesResources(String path, URL resource) {
        this.resources = new ResourcesUtils(path: path, resource: resource)
    }

    String getPath() {
        resources.path
    }

    URL getResource() {
        resources.resource
    }

    File asFile(File parent) {
        resources.asFile parent
    }

    void createFile(File parent) {
        resources.createFile parent
    }

    void createCommand(File parent) {
        resources.createCommand parent
    }

    String replaced(File parent, def search, def replace) {
        resources.replaced parent, search, replace
    }

    String toString() {
        resources.toString()
    }
}
