/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.ubuntu

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>Ubuntu cronjob</i> for background tasks configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuCronjobOwncloud_7_Config {

    private Object script

    @Inject
    private UbuntuCronjobOwncloud_7_ConfigLogger log

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    TemplateResource cronjobTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "UbuntuCronjobOwncloud_7_Config"
        cronjobTemplate = templates.getResource "cronjob"
    }

    /**
     * Deploys the <i>cronjob</i> configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void deployConfig(Domain domain, OwncloudService service) {
        if (!owncloudCronjobEnabled) {
            return
        }
        def cronjobfile = owncloudCronjobFile domain, service
        def args = [:]
        args.domain = domain
        args.service = service
        args.user = domain.domainUser
        args.script = owncloudCronScriptFile domain, service
        args.phpcommand = owncloudPhpCommand
        FileUtils.writeStringToFile cronjobfile, cronjobTemplate.getText(true, "cronjobConfig", "args", args), charset
        log.deployCronjob this, cronjobfile
    }

    /**
     * Returns is <i>cronjob</i> enabled, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_cronjob_enabled"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    boolean getOwncloudCronjobEnabled() {
        profileBooleanProperty "owncloud_cronjob_enabled", owncloudProperties
    }

    /**
     * Returns the path of the <i>php</i> command, for
     * example {@code "/usr/bin/php-cgi"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_php_command"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getOwncloudPhpCommand() {
        profileProperty "owncloud_php_command", owncloudProperties
    }

    /**
     * Returns is <i>cronjobs</i> configuration directory, for
     * example {@code "/etc/cron.d"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_cronjobs_directory"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    File getOwncloudCronjobDirectory() {
        profileDirProperty "owncloud_cronjobs_directory", owncloudProperties
    }

    /**
     * Returns is <i>cronjob</i> background task file, for
     * example {@code "<domain.name>-<service.prefix>-owncloud-background"}.
     * If the path is not absolute it is assumed to be under
     * the <i>cronjobs</i> configuration directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_cronjob_file"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getOwncloudProperties()
     */
    File owncloudCronjobFile(Domain domain, OwncloudService service) {
        def value = profileProperty "owncloud_cronjob_file", owncloudProperties
        def name = new ST(value).add("domain", domain).add("service", service).render()
        name = StringUtils.replaceChars name, ".", "_"
        def file = new File(name)
        if (!file.absolute) {
            new File(owncloudCronjobDirectory, file.name)
        } else {
            file
        }
    }

    /**
     * Returns is <i>cronjob</i> background task script file, for
     * example {@code "cron.php"}. If the path is not absolute it is assumed
     * to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_cronscript_file"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getOwncloudProperties()
     */
    File owncloudCronScriptFile(Domain domain, OwncloudService service) {
        def dir = new File(serviceDir(domain, null, service))
        profileFileProperty "owncloud_cronscript_file", dir, owncloudProperties
    }

    /**
     * Returns the default <i>ownCloud</i> properties.
     *
     * @return the <i>ownCloud</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getOwncloudProperties()

    /**
     * Returns the <i>ownCloud</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

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
