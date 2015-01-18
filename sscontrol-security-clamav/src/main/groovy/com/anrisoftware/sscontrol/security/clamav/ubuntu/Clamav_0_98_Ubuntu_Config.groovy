/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav.ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.joda.time.Duration

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.StatusServiceFactory
import com.anrisoftware.sscontrol.security.clamav.ClamavService
import com.anrisoftware.sscontrol.security.clamav.linux.Clamav_0_98_Config

/**
 * <i>ClamAV 0.98.x Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Clamav_0_98_Ubuntu_Config extends Clamav_0_98_Config {

    @Inject
    RestartServicesFactory restartServicesFactory

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    StatusServiceFactory statusServiceFactory

    TemplateResource clamavCommandsTemplate

    @Inject
    final void setUbuntuTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Clamav_0_98_Ubuntu_Config"
        this.clamavCommandsTemplate = templates.getResource "clamav_commands"
    }

    /**
     * Updates the <i>ClamAV</i> database.
     *
     * @param service
     *            the {@link ClamavService}.
     *
     * @see #getFreshclamCommand()
     */
    void updateFreshclam(ClamavService service) {
        def task = scriptExecFactory.create(
                log: log,
                freshclamCommand: freshclamCommand,
                timeout: updateFreshclamTimeout,
                this, threads, clamavCommandsTemplate, "updateFreshclam")()
    }

    /**
     * Restarts the <i>ClamAV</i> service.
     *
     * @param service
     *            the {@link ClamavService}.
     *
     * @see #getClamavRestartCommand()
     * @see #getClamavRestartServices()
     * @see #getClamavRestartFlags()
     */
    void restartClamav(ClamavService service) {
        restartServicesFactory.create(
                log: log,
                command: clamavRestartCommand,
                services: clamavRestartServices,
                flags: clamavRestartFlags,
                timeout: clamavRestartTimeout,
                this, threads)()
        statusServiceFactory.create(
                log: log,
                command: clamavStatusCommand,
                service: clamavStatusService,
                flags: clamavStatusFlags,
                this, threads)()
    }

    /**
     * Restarts the <i>Freshclam</i> service.
     *
     * @param service
     *            the {@link ClamavService}.
     *
     * @see #getFreshclamRestartCommand()
     * @see #getFreshclamRestartServices()
     * @see #getFreshclamRestartFlags()
     */
    void restartFreshclam(ClamavService service) {
        def task = restartServicesFactory.create(
                log: log,
                command: freshclamRestartCommand,
                services: freshclamRestartServices,
                flags: freshclamRestartFlags,
                this, threads)()
    }

    /**
     * Installs the <i>ClamAV</i> packages.
     *
     * @see #getClamavPackages()
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: clamavPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Returns the <i>ClamAV</i> restart command, for
     * example {@code "/etc/init.d/clamav-daemon"}
     *
     * <ul>
     * <li>profile property {@code "clamav_daemon_restart_command"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getClamavRestartCommand() {
        profileProperty "clamav_daemon_restart_command", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> status command, for
     * example {@code "/etc/init.d/clamav-daemon"}
     *
     * <ul>
     * <li>profile property {@code "clamav_daemon_status_command"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getClamavStatusCommand() {
        profileProperty "clamav_daemon_status_command", clamavProperties
    }

    /**
     * Returns the <i>Freshclam</i> restart command, for
     * example {@code "/etc/init.d/clamav-freshclam"}
     *
     * <ul>
     * <li>profile property {@code "freshclam_restart_command"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getFreshclamRestartCommand() {
        profileProperty "freshclam_restart_command", clamavProperties
    }

    /**
     * Returns the <i>Freshclam</i> restart command, for
     * example {@code "/usr/bin/freshclam"}
     *
     * <ul>
     * <li>profile property {@code "freshclam_command"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getFreshclamCommand() {
        profileProperty "freshclam_command", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> restart services, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "clamav_restart_services"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    List getClamavRestartServices() {
        profileListProperty "clamav_restart_services", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "clamav_restart_flags"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getClamavRestartFlags() {
        profileProperty "clamav_restart_flags", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> restart timeout duration, for
     * example {@code "PT30M"}
     *
     * <ul>
     * <li>profile property {@code "clamav_restart_timeout"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    Duration getClamavRestartTimeout() {
        profileDurationProperty "clamav_restart_timeout", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> status service, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "clamav_status_service"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getClamavStatusService() {
        profileProperty "clamav_status_service", clamavProperties
    }

    /**
     * Returns the <i>ClamAV</i> status flags, for
     * example {@code "status"}
     *
     * <ul>
     * <li>profile property {@code "clamav_status_flags"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getClamavStatusFlags() {
        profileProperty "clamav_status_flags", clamavProperties
    }

    /**
     * Returns the <i>Freshclam</i> restart services, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "freshclam_restart_services"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    List getFreshclamRestartServices() {
        profileListProperty "freshclam_restart_services", clamavProperties
    }

    /**
     * Returns the <i>Freshclam</i> restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "Freshclam_restart_flags"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    String getFreshclamRestartFlags() {
        profileProperty "freshclam_restart_flags", clamavProperties
    }

    /**
     * Returns the {@code ClamAV} service packages, for
     * example {@code "clamav-daemon"}
     *
     * <ul>
     * <li>profile property {@code "clamav_packages"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    List getClamavPackages() {
        profileListProperty "clamav_packages", clamavProperties
    }

    /**
     * Returns the update database timeout duration, for
     * example {@code "PT2H"}
     *
     * <ul>
     * <li>profile property {@code "update_freshclam_timeout"}</li>
     * </ul>
     *
     * @see #getClamavProperties()
     */
    Duration getUpdateFreshclamTimeout() {
        profileDurationProperty "update_freshclam_timeout", clamavProperties
    }
}
