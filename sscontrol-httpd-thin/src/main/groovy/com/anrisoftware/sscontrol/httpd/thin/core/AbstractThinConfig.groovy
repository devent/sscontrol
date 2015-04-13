/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-thin.
 *
 * sscontrol-httpd-thin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-thin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-thin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.thin.core

import groovy.util.logging.Slf4j

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Thin</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class AbstractThinConfig {

    private Object script

    /**
     * Returns the <i>Thin</i> service chdir directory, for
     * example {@code "/var/www/domain.com/redmineprefix"}
     * <p>
     * The following place holders are replaced:
     * <ul>
     * <li>{@code domainName}, the domain name;</li>
     * <li>{@code servicePrefix}, the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    File domainChdirDir(Domain domain, WebService service) {
        def name = profileProperty "thin_domain_configuration_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(confDir, file)
    }

    /**
     * Returns the <i>Thin</i> service configuration file, for
     * example {@code "/etc/thin1.8/<domainName>_<servicePrefix>.yml".}
     * <p>
     * The following place holders are replaced:
     * <ul>
     * <li>{@code domainName}, the domain name;</li>
     * <li>{@code servicePrefix}, the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "thin_domain_configuration_file"}</li>
     * </ul>
     *
     * @see #getThinConfDir()
     */
    File domainConfigurationFile(Domain domain, WebService service) {
        def name = profileProperty "thin_domain_configuration_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(confDir, file)
    }

    /**
     * Returns the <i>Thin</i> service log file, for
     * example {@code "/var/log/thin/<domainName>_<servicePrefix>.log".}
     * <p>
     * The following place holders are replaced:
     * <ul>
     * <li>{@code domainName}, the domain name;</li>
     * <li>{@code servicePrefix}, the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "thin_domain_log_file"}</li>
     * </ul>
     *
     * @see #getThinLogDir()
     */
    File domainLogFile(Domain domain, WebService service) {
        def name = profileProperty "thin_domain_log_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(logDir, file)
    }

    /**
     * Returns the <i>Thin</i> service pid file, for
     * example {@code "/var/run/thin/<domainName>_<servicePrefix>.pid".}
     * <p>
     * The following place holders are replaced:
     * <ul>
     * <li>{@code domainName}, the domain name;</li>
     * <li>{@code servicePrefix}, the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "thin_domain_pid_file"}</li>
     * </ul>
     *
     * @see #getThinRunDir()
     */
    File domainPidFile(Domain domain, WebService service) {
        def name = profileProperty "thin_domain_pid_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(runDir, file)
    }

    /**
     * Returns the <i>Thin</i> service socket file, for
     * example {@code "/var/run/thin/<domainName>_<servicePrefix>.sock".}
     * <p>
     * The following place holders are replaced:
     * <ul>
     * <li>{@code domainName}, the domain name;</li>
     * <li>{@code servicePrefix}, the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "thin_domain_socket_file"}</li>
     * </ul>
     *
     * @see #getThinRunDir()
     */
    File domainSocketFile(Domain domain, WebService service) {
        def name = profileProperty "thin_domain_socket_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(runDir, file)
    }

    /**
     * Returns the <i>Thin</i> command, for example {@code "/usr/bin/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_command"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinCommand() {
        profileProperty "thin_command", thinProperties
    }

    /**
     * Returns the <i>Thin</i> restart command, for
     * example {@code "/etc/init.d/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_restart_command"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinRestartCommand() {
        profileProperty "thin_restart_command", thinProperties
    }

    /**
     * Returns the <i>Thin</i> services to restart, for
     * example {@code "".}
     *
     * <ul>
     * <li>profile property {@code "thin_restart_services"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    List getThinRestartServices() {
        profileListProperty "thin_restart_services", thinProperties
    }

    /**
     * Returns the <i>Thin</i> restart flags, for
     * example {@code "restart".}
     *
     * <ul>
     * <li>profile property {@code "thin_restart_flags"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinRestartFlags() {
        profileProperty "thin_restart_flags", thinProperties
    }

    /**
     * Returns the <i>Thin</i> stop command, for
     * example {@code "/etc/init.d/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_stop_command"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinStopCommand() {
        profileProperty "thin_stop_command", thinProperties
    }

    /**
     * Returns the <i>Thin</i> services to stop, for
     * example {@code "".}
     *
     * <ul>
     * <li>profile property {@code "thin_stop_services"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    List getThinStopServices() {
        profileListProperty "thin_stop_services", thinProperties
    }

    /**
     * Returns the <i>Thin</i> stop flags, for
     * example {@code "stop".}
     *
     * <ul>
     * <li>profile property {@code "thin_stop_flags"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinStopFlags() {
        profileProperty "thin_stop_flags", thinProperties
    }

    /**
     * Returns the <i>Thin</i> script, for example {@code "/etc/init.d/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_script_file"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    File getThinScriptFile() {
        profileDirProperty "thin_script_file", thinProperties
    }

    /**
     * Returns the <i>Thin</i> defaults file, for
     * example {@code "/etc/default/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_defaults_file"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    File getThinDefaultsFile() {
        profileDirProperty "thin_defaults_file", thinProperties
    }

    /**
     * Returns the <i>Thin</i> configuration directory, for
     * example {@code "/etc/thin1.8".}
     *
     * <ul>
     * <li>profile property {@code "thin_configuration_directory"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    File getConfDir() {
        profileDirProperty "thin_configuration_directory", thinProperties
    }

    /**
     * Returns the <i>Thin</i> log directory, for
     * example {@code "/var/log/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_log_directory"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    File getLogDir() {
        profileDirProperty "thin_log_directory", thinProperties
    }

    /**
     * Returns the <i>Thin</i> run directory, for
     * example {@code "/var/run/thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_run_directory"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    File getRunDir() {
        profileDirProperty "thin_run_directory", thinProperties
    }

    /**
     * Returns the <i>Thin</i> user, for
     * example {@code "thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_user"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinUser() {
        profileProperty "thin_user", thinProperties
    }

    /**
     * Returns the <i>Thin</i> user group, for
     * example {@code "thin".}
     *
     * <ul>
     * <li>profile property {@code "thin_group"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    String getThinGroup() {
        profileProperty "thin_group", thinProperties
    }

    /**
     * Returns the additional <i>Thin</i> user groups, for
     * example {@code "foo, bar"}
     *
     * <ul>
     * <li>profile property {@code "thin_additional_groups"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    List getThinAdditionalGroups() {
        profileListProperty "thin_additional_groups", thinProperties
    }

    /**
     * Returns the <i>Thin</i> timeout duration, for
     * example {@code "PT30S".}
     *
     * <ul>
     * <li>profile property {@code "thin_timeout_duration"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    Duration getTimeoutDuration() {
        profileDurationProperty "thin_timeout_duration", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default maximum connections, for
     * example {@code "1024".}
     *
     * <ul>
     * <li>profile property {@code "thin_max_connections"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    int getMaxConnections() {
        profileNumberProperty "thin_max_connections", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default maximum persistent connections, for
     * example {@code "512".}
     *
     * <ul>
     * <li>profile property {@code "thin_max_persistent_connections"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    int getMaxPersistentConnections() {
        profileNumberProperty "thin_max_persistent_connections", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default wait duration, for
     * example {@code "PT30S".}
     *
     * <ul>
     * <li>profile property {@code "thin_wait_duration"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    Duration getWaitDuration() {
        profileDurationProperty "thin_wait_duration", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default servers count, for
     * example {@code "1".}
     *
     * <ul>
     * <li>profile property {@code "thin_servers_count"}</li>
     * </ul>
     *
     * @see #getThinProperties()
     */
    int getServersCount() {
        profileNumberProperty "thin_servers_count", thinProperties
    }

    /**
     * Returns the domain name as a file name.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @return the file name.
     */
    String domainNameAsFileName(Domain domain) {
        domain.name.replaceAll(/\./, "_")
    }

    /**
     * Returns the <i>Thin</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getThinProperties()

    /**
     * Returns the service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
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
