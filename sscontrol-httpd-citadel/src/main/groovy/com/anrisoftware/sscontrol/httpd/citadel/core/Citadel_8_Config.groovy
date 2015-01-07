/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.core

import groovy.util.logging.Slf4j

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.citadel.AuthMethod
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService

/**
 * <i>Citadel 8</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Citadel_8_Config {

    /**
     * @see ServiceConfig#getScript()
     */
    private Object script

    /**
     * Setups default options.
     *
     * @param service
     *            the {@link CitadelService}.
     */
    void setupDefaults(CitadelService service) {
        if (service.authMethod == null) {
            service.auth method: defaultAuthMethod
        }
        if (service.bindingAddresses == null) {
            service.bind defaultBindingAddress, port: defaultBindingPort
        }
    }

    /**
     * Returns the list of needed packages for <i>Citadel</i> service, for
     * example {@code "citadel-suite, expect"}
     *
     * <ul>
     * <li>profile property {@code "citadel_packages"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    List getCitadelPackages() {
        profileListProperty "citadel_packages", citadelProperties
    }

    /**
     * Returns the <i>Citadel</i> setup command, for
     * example {@code "/usr/lib/citadel-server/setup"}
     *
     * <ul>
     * <li>profile property {@code "citadel_setup_command"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getCitadelSetupCommand() {
        profileProperty "citadel_setup_command", citadelProperties
    }

    /**
     * Returns the <i>Citadel</i> restart command, for
     * example {@code "/etc/init.d/citadel"}
     *
     * <ul>
     * <li>profile property {@code "citadel_restart_command"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getCitadelRestartCommand() {
        profileProperty "citadel_restart_command", citadelProperties
    }

    /**
     * Returns the <i>expect</i> command path, for
     * example {@code "/usr/bin/expect"}
     *
     * <ul>
     * <li>profile property {@code "expect_command"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getExpectCommand() {
        profileProperty "expect_command", citadelProperties
    }

    /**
     * Returns the timeout duration to setup process, for
     * example {@code "PT60S".}
     *
     * <ul>
     * <li>profile property {@code "citadel_setup_timeout"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    Duration getCitadelSetupTimeout() {
        profileDurationProperty "citadel_setup_timeout", citadelProperties
    }

    /**
     * Returns the local user under which the <i>Citadel</i> service runs, for
     * example {@code "citadel".}
     *
     * <ul>
     * <li>profile property {@code "citadel_user"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getCitadelUser() {
        profileProperty "citadel_user", citadelProperties
    }

    /**
     * Returns disable the <i>Nsswitch Db</i> service, for
     * example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "nsswitch_db_disable"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    boolean getNsswitchDbDisable() {
        profileBooleanProperty "nsswitch_db_disable", citadelProperties
    }

    /**
     * Returns the default authentication method, for
     * example {@code "hostSystem"}
     *
     * <ul>
     * <li>profile property {@code "citadel_default_auth_method"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    AuthMethod getDefaultAuthMethod() {
        def value = profileProperty "citadel_default_auth_method", citadelProperties
        AuthMethod.valueOf(value)
    }

    /**
     * Returns the default <i>Citadel</i> binding address, for
     * example {@code "127.0.0.1"}
     *
     * <ul>
     * <li>profile property {@code "citadel_default_binding_address"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getDefaultBindingAddress() {
        profileProperty "citadel_default_binding_address", citadelProperties
    }

    /**
     * Returns the default <i>Citadel</i> binding port, for
     * example {@code "504"}
     *
     * <ul>
     * <li>profile property {@code "citadel_default_binding_port"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    int getDefaultBindingPort() {
        profileNumberProperty "citadel_default_binding_port", citadelProperties
    }

    /**
     * Returns the default <i>Citadel</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getCitadelProperties()

    /**
     * Returns the <i>Citadel</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
