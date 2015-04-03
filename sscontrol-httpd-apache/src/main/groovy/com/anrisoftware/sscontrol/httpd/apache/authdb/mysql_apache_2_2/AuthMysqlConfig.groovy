/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authdb.mysql_apache_2_2

import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.apache.authdb.core.DbDriverConfig
import com.anrisoftware.sscontrol.httpd.apache.authldap.apache_2_2.RequireValidModeRenderer
import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.authdb.AuthDbService
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Auth-Database Mysql</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthMysqlConfig extends DbDriverConfig {

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-db"

    /**
     * Authentication database driver name.
     */
    public static final String DRIVER_NAME = "mysql"

    @Inject
    private AuthMysqlLogger log

    @Inject
    RequireValidModeRenderer requireValidModeRenderer

    /**
     * Domain configuration template.
     */
    TemplateResource domainConfigTemplate

    @Inject
    final void TemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_AuthMysqlConfig", ["renderers": [requireValidModeRenderer]]
        this.domainConfigTemplate = templates.getResource "domain"
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        deployDomain domain, null, service, config
        enableAuthMods service
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        createDomainConfig domain, service, config
    }

    /**
     * Sets the service defaults.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link AuthDbService} service.
     */
    void setupDefaults(Domain domain, AuthDbService service) {
        setupDefaultDatabase(domain, service)
        if (service.authoritative == null) {
            service.type AuthType.basic, authoritative: defaultAuthoritative
        }
        if (service.usersTable == null) {
            service.users table: defaultUsersTable
        }
        if (service.userNameField == null) {
            service.field userName: defaultUserField
        }
        if (service.passwordField == null) {
            service.field password: defaultPasswordField
        }
        if (service.allowEmptyPasswords == null) {
            service.allow emptyPasswords: defaultAllowEmptyPasswords
        }
    }

    /**
     * Sets the service database defaults.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link AuthDbService} service.
     */
    void setupDefaultDatabase(Domain domain, AuthDbService service) {
        if (service.database == null) {
            throw log.databaseNull(this, domain, service)
        }
        def database = service.database.database
        if (service.database.host == null && !StringUtils.isEmpty(defaultDatabaseHost)) {
            service.database database, host: defaultDatabaseHost
        }
        if (service.database.port == null && defaultDatabasePort > 0) {
            service.database database, port: defaultDatabasePort
        }
        if (service.database.encryption == null) {
            service.database database, encryption: defaultDatabaseEncryptions
        }
    }

    /**
     * Create the domain configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link AuthDbService} service.
     *
     * @param serviceConfig
     *            the {@link List} of the configuration.
     */
    void createDomainConfig(Domain domain, AuthDbService service, List serviceConfig) {
        def args = [:]
        args.auth = service.auth
        args.location = service.location
        args.database = service.database
        args.usersTable = service.usersTable
        args.userNameField = service.userNameField
        args.passwordField = service.passwordField
        args.allowEmptyPasswords = service.allowEmptyPasswords
        args.authoritative = service.authoritative
        args.requireValids = service.requireValid
        args.requireGroups = service.requireGroups
        args.requireUsers = service.requireUsers
        args.exceptLimits = service.requireExcept
        def config = domainConfigTemplate.getText true, "domainAuth", "args", args
        serviceConfig << config
    }

    /**
     * Enables the Apache auth mods.
     *
     * @param service
     *            the {@link AuthDbService} service.
     */
    void enableAuthMods(AuthDbService service) {
        enableMods(["auth_mysql"])
    }

    /**
     * Returns the {@code mysql} command,
     * for example {@code "/usr/bin/mysql"}.
     *
     * <ul>
     * <li>profile property {@code "mysql_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getMysqlCommand() {
        profileProperty "mysql_command", defaultProperties
    }

    /**
     * Returns the default authoritative flag, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_authoritative"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    boolean getDefaultAuthoritative() {
        profileBooleanProperty "auth_mysql_default_authoritative", authProperties
    }

    /**
     * Returns the default database host, for example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_database_host"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getDefaultDatabaseHost() {
        profileProperty "auth_mysql_default_database_host", authProperties
    }

    /**
     * Returns the default database port, for example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_database_port"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    int getDefaultDatabasePort() {
        profileNumberProperty "auth_mysql_default_database_port", authProperties
    }

    /**
     * Returns the default database encryption method, for example {@code "PHP_MD5, Crypt"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_database_encryptions"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    List getDefaultDatabaseEncryptions() {
        profileListProperty "auth_mysql_default_database_encryptions", authProperties
    }

    /**
     * Returns the default users table name, for example {@code "mysql_auth"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_users_table"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getDefaultUsersTable() {
        profileProperty "auth_mysql_default_users_table", authProperties
    }

    /**
     * Returns the default user field name, for example {@code "username"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_user_field"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getDefaultUserField() {
        profileProperty "auth_mysql_default_user_field", authProperties
    }

    /**
     * Returns the default password field name, for example {@code "password"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_password_field"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getDefaultPasswordField() {
        profileProperty "auth_mysql_default_password_field", authProperties
    }

    /**
     * Returns the default allow empty passwords, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "auth_mysql_default_allow_empty_passwords"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    boolean getDefaultAllowEmptyPasswords() {
        profileBooleanProperty "auth_mysql_default_allow_empty_passwords", authProperties
    }

    /**
     * Returns the auth properties.
     */
    abstract ContextProperties getAuthProperties()

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    String getServiceName() {
        SERVICE_NAME
    }
}
