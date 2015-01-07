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
package com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * <i>Webcit 8 Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Webcit_8_Ubuntu_Config {

    @Inject
    private Webcit_8_Ubuntu_ConfigLogger logg

    /**
     * @see ServiceConfig#getScript()
     */
    private Object script

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    TemplateResource webcitDefaultsConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Webcit_8_Ubuntu_Config"
        this.webcitDefaultsConfigTemplate = templates.getResource "defaults_config"
    }

    /**
     * Deploys the <i>Webcit</i> defaults configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @see #getWebcitDefaultsFile()
     * @see #getCitadelProperties()
     */
    void deployWebcitDefaultConfig(Domain domain, CitadelService service) {
        def configs = [
            configToken("citadelAddressConfig", "addresses", service.bindingAddresses),
            configToken("citadelPortConfig", "addresses", service.bindingAddresses),
            configToken("httpPortConfig", "port", webcitHttpPort),
            configToken("httpsPortConfig", "port", webcitHttpsPort),
            configToken("webcitAddressConfig", "address", webcitAddress),
        ]
        def file = webcitDefaultsFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    TokenTemplate configToken(Object[] args) {
        def search = webcitDefaultsConfigTemplate.getText(true, "${args[0]}Search")
        def replace = webcitDefaultsConfigTemplate.getText(true, args)
        new TokenTemplate(search, replace)
    }

    /**
     * Restarts the <i>Webcit</i> service.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @see #getCitadelProperties()
     */
    void restartWebcit(CitadelService service) {
        def task = restartServicesFactory.create(
                log: log,
                command: webcitRestartCommand,
                services: [],
                flags: webcitRestartFlags,
                this, threads)()
    }

    /**
     * Returns the <i>Webcit</i> defaults file, for
     * example {@code "/etc/default/webcit"}
     *
     * <ul>
     * <li>profile property {@code "webcit_defaults_file"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    File getWebcitDefaultsFile() {
        profileProperty("webcit_defaults_file", citadelProperties) as File
    }

    /**
     * Returns the <i>Webcit</i> restart command, for
     * example {@code "/etc/init.d/webcit"}
     *
     * <ul>
     * <li>profile property {@code "webcit_restart_command"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getWebcitRestartCommand() {
        profileProperty "webcit_restart_command", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "webcit_restart_flags"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getWebcitRestartFlags() {
        profileProperty "webcit_restart_flags", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> HTTP port, for
     * example {@code "9090"}
     *
     * <ul>
     * <li>profile property {@code "webcit_http_port"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    int getWebcitHttpPort() {
        profileNumberProperty "webcit_http_port", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> HTTPS port, for
     * example {@code "9092"}
     *
     * <ul>
     * <li>profile property {@code "webcit_https_port"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    int getWebcitHttpsPort() {
        profileNumberProperty "webcit_https_port", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> listen address, for
     * example {@code "127.0.0.1"}
     *
     * <ul>
     * <li>profile property {@code "webcit_address"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getWebcitAddress() {
        profileProperty "webcit_address", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> list subscription location, for
     * example {@code "listsub/"}
     *
     * <ul>
     * <li>profile property {@code "webcit_listsub_location"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getListsubLocation() {
        profileProperty "webcit_listsub_location", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> <i>Groupdav</i> location, for
     * example {@code "groupdav/"}
     *
     * <ul>
     * <li>profile property {@code "webcit_groupdav_location"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getGroupdavLocation() {
        profileProperty "webcit_groupdav_location", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> free busy location, for
     * example {@code "freebusy/"}
     *
     * <ul>
     * <li>profile property {@code "webcit_freebusy_location"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getFreebusyLocation() {
        profileProperty "webcit_freebusy_location", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> proxy connect timeout duration, for
     * example {@code "PS90S"}
     *
     * <ul>
     * <li>profile property {@code "webcit_proxy_connect_timeout"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    Duration getWebcitProxyConnectTimeout() {
        profileDurationProperty "webcit_proxy_connect_timeout", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> proxy send timeout duration, for
     * example {@code "PS90S"}
     *
     * <ul>
     * <li>profile property {@code "webcit_proxy_send_timeout"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    Duration getWebcitProxySendTimeout() {
        profileDurationProperty "webcit_proxy_send_timeout", citadelProperties
    }

    /**
     * Returns the <i>Webcit</i> proxy read timeout duration, for
     * example {@code "PS90S"}
     *
     * <ul>
     * <li>profile property {@code "webcit_proxy_read_timeout"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    Duration getWebcitProxyReadTimeout() {
        profileDurationProperty "webcit_proxy_read_timeout", citadelProperties
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
