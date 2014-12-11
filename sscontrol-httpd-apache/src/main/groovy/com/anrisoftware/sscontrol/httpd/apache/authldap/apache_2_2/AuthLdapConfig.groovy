/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authldap.apache_2_2

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService
import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Auth-LDAP</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthLdapConfig extends BasicAuth {

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-ldap"

    @Inject
    AuthLdapConfigLogger log

    @Inject
    RequireValidModeRenderer requireValidModeRenderer

    /**
     * Auth/ldap configuration.
     */
    TemplateResource authDomainTemplate

    /**
     * Auth/ldap commands.
     */
    TemplateResource authCommandsTemplate

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaultProperties service
        createDomainConfig domain, service, config
        enableMods service
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        createDomainConfig domain, service, config
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_AuthLdap", ["renderers": [requireValidModeRenderer]]
        this.authDomainTemplate = templates.getResource "domain"
    }

    /**
     * Sets the default properties for the service.
     *
     * @param service
     *            the {@link AuthService}.
     */
    void setupDefaultProperties(AbstractAuthService service) {
        service.type = AuthType.basic
        service.authoritative ? service.authoritative : defaultAuthoritative
    }

    /**
     * Create the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link AuthService}.
     *
     * @param serviceConfig
     *            the {@link List} of the configuration.
     */
    void createDomainConfig(Domain domain, AbstractAuthService service, List serviceConfig) {
        def config = authDomainTemplate.getText true,
                "domainAuth",
                "domain", domain,
                "auth", service,
                "args", []
        log.domainConfigCreated script, service, config
        serviceConfig << config
    }

    /**
     * Enables the Apache mods.
     *
     * @param service
     *            the {@link AuthService}.
     */
    void enableMods(AbstractAuthService service) {
        switch (service.type) {
            case AuthType.basic:
                enableMods(["authnz_ldap", "auth_basic"])
                break
        }
    }

    /**
     * Returns the default authoritative LDAP flag.
     * For example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "default_authoritative"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    boolean getDefaultAuthoritative() {
        profileBooleanProperty "default_authoritative", authProperties
    }

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    String getServiceName() {
        SERVICE_NAME
    }
}
