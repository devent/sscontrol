/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.wordpress.apache_2_2

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService

/**
 * <i>Apache 2.2 Wordpress</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Apache_2_2_WordpressConfig {

    private Object script

    @Inject
    Apache_2_2_FcgiWordpressConfig fcgiOwncloudConfig

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiOwncloudConfig.deployDomain domain, refDomain, service, config
    }

    void deployService(Domain domain, WebService service, List config) {
        enableMods wordpressApacheMods
        fcgiOwncloudConfig.deployService domain, service, config
    }

    /**
     * Returns the <i>Apache</i> mods, for
     * example {@code "rewrite"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_apache_mods"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    List getWordpressApacheMods() {
        profileListProperty "wordpress_apache_mods", wordpressProperties
    }

    /**
     * Returns the <i>Wordpress</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see WordpressService#getPrefix()
     */
    File owncloudDir(Domain domain, WordpressService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>Wordpress</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getWordpressProperties()

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
        fcgiOwncloudConfig.setScript this
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the service profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
