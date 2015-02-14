/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory

/**
 * <i>Redmine</i> main configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Redmine_2_ConfigurationConfig {

    private Object script

    @Inject
    Redmine_2_ConfigurationConfigLogger logg

    @Inject
    MailConfigRepresenter mailConfigRepresenter

    TemplateResource configTemplate

    /**
     * Deploys the <i>Redmine</i> database configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineConfigExampleFile()
     * @see #getRedmineConfigFile()
     */
    void deployEmail(Domain domain, RedmineService service) {
        def exampleFile = redmineConfigFile domain, service, redmineConfigExampleFile
        def file = redmineConfigFile domain, service, redmineConfigFile
        file.isFile() == false ? FileUtils.copyFile(exampleFile, file) : false
        def conf = currentConfiguration file
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle DumperOptions.FlowStyle.BLOCK
        Yaml yaml = new Yaml(mailConfigRepresenter, options);
        Map map = yaml.load(conf)
        Map defaultMap = map["default"]
        if (!defaultMap.email_delivery) {
            defaultMap.email_delivery = [:]
        }
        if (!defaultMap.email_delivery.smtp_settings) {
            defaultMap.email_delivery.smtp_settings = [:]
        }
        if (service.mail.method) {
            defaultMap.email_delivery.delivery_method = service.mail.method
        } else {
            defaultMap.email_delivery.remove "delivery_method"
        }
        if (service.mail.host) {
            defaultMap.email_delivery.smtp_settings.address = service.mail.host
        } else {
            defaultMap.email_delivery.smtp_settings.remove "address"
        }
        if (service.mail.port) {
            defaultMap.email_delivery.smtp_settings.port = service.mail.port
        } else {
            defaultMap.email_delivery.smtp_settings.remove "port"
        }
        if (service.mail.domain) {
            defaultMap.email_delivery.smtp_settings.domain = service.mail.domain
        } else {
            defaultMap.email_delivery.smtp_settings.remove "domain"
        }
        if (service.mail.auth) {
            defaultMap.email_delivery.smtp_settings.authentication = service.mail.auth
        } else {
            defaultMap.email_delivery.smtp_settings.remove "authentication"
        }
        if (service.mail.user) {
            defaultMap.email_delivery.smtp_settings.user_name = service.mail.user
        } else {
            defaultMap.email_delivery.smtp_settings.remove "user_name"
        }
        if (service.mail.password) {
            defaultMap.email_delivery.smtp_settings.password = service.mail.password
        } else {
            defaultMap.email_delivery.smtp_settings.remove "password"
        }
        conf = configTemplate.getText "configHead", "service", service
        conf += yaml.dump map
        FileUtils.writeStringToFile file, conf, charset
        logg.configDeployed this, file, conf
    }

    /**
     * Returns the <i>Redmine</i> configuration file property, for
     * example {@code "config/configuration.yml".}
     *
     * <ul>
     * <li>profile property {@code "redmine_configuration_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRedmineConfigFile() {
        profileProperty "redmine_configuration_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> example configuration file property, for
     * example {@code "config/configuration.yml.example".}
     *
     * <ul>
     * <li>profile property {@code "redmine_configuration_example_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRedmineConfigExampleFile() {
        profileProperty "redmine_configuration_example_file", redmineProperties
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Redmine_2_5_Config"
        this.configTemplate = templates.getResource "config"
    }

    /**
     * Returns the <i>Redmine</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineProperties()

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

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
