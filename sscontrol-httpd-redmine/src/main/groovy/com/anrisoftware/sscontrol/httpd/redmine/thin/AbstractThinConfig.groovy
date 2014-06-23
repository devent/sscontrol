/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.thin

import groovy.util.logging.Slf4j

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04.RedmineConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Thin</i> <i>Redmine</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class AbstractThinConfig {

    /**
     * @see ServiceConfig#getScript()
     */
    Object script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Returns the <i>Thin</i> service chdir directory, for
     * example {@code "/var/www/domain.com/redmineprefix".}
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    File domainChdirDir(Domain domain, RedmineService service) {
        def name = profileProperty "redmine_thin_domain_configuration_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(confDir, file)
    }

    /**
     * Returns the <i>Thin</i> service configuration file, for
     * example {@code "/etc/thin1.8/<domainName>_<servicePrefix>.yml".}
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_domain_configuration_file"}</li>
     * </ul>
     *
     * @see #getThinConfDir()
     */
    File domainConfigurationFile(Domain domain, RedmineService service) {
        def name = profileProperty "redmine_thin_domain_configuration_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(confDir, file)
    }

    /**
     * Returns the <i>Thin</i> service log file, for
     * example {@code "/var/log/thin/<domainName>_<servicePrefix>.log".}
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_domain_log_file"}</li>
     * </ul>
     *
     * @see #getThinLogDir()
     */
    File domainLogFile(Domain domain, RedmineService service) {
        def name = profileProperty "redmine_thin_domain_log_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(logDir, file)
    }

    /**
     * Returns the <i>Thin</i> service pid file, for
     * example {@code "/var/run/thin/<domainName>_<servicePrefix>.pid".}
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_domain_pid_file"}</li>
     * </ul>
     *
     * @see #getThinRunDir()
     */
    File domainPidFile(Domain domain, RedmineService service) {
        def name = profileProperty "redmine_thin_domain_pid_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(runDir, file)
    }

    /**
     * Returns the <i>Thin</i> service socket file, for
     * example {@code "/var/run/thin/<domainName>_<servicePrefix>.sock".}
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_domain_socket_file"}</li>
     * </ul>
     *
     * @see #getThinRunDir()
     */
    File domainSocketFile(Domain domain, RedmineService service) {
        def name = profileProperty "redmine_thin_domain_socket_file", thinProperties
        def domainName = domainNameAsFileName domain
        def file = new ST(name).add("domainName", domainName).add("servicePrefix", service.prefix).render()
        new File(runDir, file)
    }

    /**
     * Returns the <i>thin</i> command, for example {@code "/usr/bin/thin".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_command"}</li>
     * </ul>
     */
    String getThinCommand() {
        profileProperty "redmine_thin_command", thinProperties
    }

    /**
     * Returns the <i>Thin</i> configuration directory, for
     * example {@code "/etc/thin1.8".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_configuration_directory"}</li>
     * </ul>
     */
    File getConfDir() {
        profileProperty("redmine_thin_configuration_directory", thinProperties) as File
    }

    /**
     * Returns the <i>Thin</i> log directory, for
     * example {@code "/var/log/thin".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_log_directory"}</li>
     * </ul>
     */
    File getLogDir() {
        profileProperty("redmine_thin_log_directory", thinProperties) as File
    }

    /**
     * Returns the <i>Thin</i> run directory, for
     * example {@code "/var/run/thin".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_run_directory"}</li>
     * </ul>
     */
    File getRunDir() {
        profileProperty("redmine_thin_run_directory", thinProperties) as File
    }

    /**
     * Returns the <i>Thin</i> user, for
     * example {@code "thin".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_user"}</li>
     * </ul>
     */
    String getThinUser() {
        profileProperty "redmine_thin_user", thinProperties
    }

    /**
     * Returns the <i>Thin</i> user group, for
     * example {@code "thin".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_group"}</li>
     * </ul>
     */
    String getThinGroup() {
        profileProperty "redmine_thin_group", thinProperties
    }

    /**
     * Returns the <i>Thin</i> timeout duration, for
     * example {@code "PT30S".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_timeout_duration"}</li>
     * </ul>
     */
    Duration getTimeoutDuration() {
        profileDurationProperty "redmine_thin_timeout_duration", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default maximum connections, for
     * example {@code "1024".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_max_connections"}</li>
     * </ul>
     */
    int getMaxConnections() {
        profileNumberProperty "redmine_thin_max_connections", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default maximum persistent connections, for
     * example {@code "512".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_max_persistent_connections"}</li>
     * </ul>
     */
    int getMaxPersistentConnections() {
        profileNumberProperty "redmine_thin_max_persistent_connections", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default wait duration, for
     * example {@code "PT30S".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_wait_duration"}</li>
     * </ul>
     */
    Duration getWaitDuration() {
        profileDurationProperty "redmine_thin_wait_duration", thinProperties
    }

    /**
     * Returns the <i>Thin</i> default servers count, for
     * example {@code "1".}
     *
     * <ul>
     * <li>profile property {@code "redmine_thin_servers_count"}</li>
     * </ul>
     */
    int getServersCount() {
        profileNumberProperty "redmine_thin_servers_count", thinProperties
    }

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    /**
     * Returns the <i>Thin</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getThinProperties()

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
