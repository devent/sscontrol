/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Fail2ban Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UbuntuConfig {

    private LinuxScript script

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    /**
     * Installs the <i>fail2ban</i> packages.
     *
     * @see #getFail2banPackages()
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: fail2banPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Restarts the <i>fail2ban</i> services.
     *
     * @see #getFail2banRestartCommand()
     * @see #getFail2banRestartServices()
     * @see #getFail2banRestartFlags()
     */
    void restartServices() {
        restartServicesFactory.create(
                log: log,
                command: fail2banRestartCommand,
                services: fail2banRestartServices,
                flags: fail2banRestartFlags,
                this, threads)()
    }

    /**
     * Returns the {@code Fail2ban} service packages, for
     * example {@code "fail2ban"}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_packages"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    List getFail2banPackages() {
        profileListProperty "fail2ban_packages", fail2banProperties
    }

    /**
     * Returns the {@code Fail2ban} service restart command, for
     * example {@code "/etc/init.d/fail2ban"}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_restart_command"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    String getFail2banRestartCommand() {
        profileDirProperty "fail2ban_restart_command", fail2banProperties
    }

    /**
     * Returns the {@code Fail2ban} services to restart, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "rsyslog_restart_services"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    List getFail2banRestartServices() {
        profileListProperty "fail2ban_restart_services", fail2banProperties
    }

    /**
     * Returns the {@code Fail2ban} service restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_restart_flags"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    String getFail2banRestartFlags() {
        profileProperty "fail2ban_restart_flags", fail2banProperties
    }

    /**
     * Returns the default <i>Fail2ban</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFail2banProperties()

    /**
     * Returns the <i>Fail2ban</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript getScript() {
        script
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
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
