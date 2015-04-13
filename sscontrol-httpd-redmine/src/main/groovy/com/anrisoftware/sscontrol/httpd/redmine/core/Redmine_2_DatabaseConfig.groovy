/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory;

/**
 * <i>Redmine</i> database configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Redmine_2_DatabaseConfig {

    private Object script

    @Inject
    Redmine_2_DatabaseConfigLogger logg

    TemplateResource databaseTemplate

    /**
     * Deploys the <i>Redmine</i> database configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineDatabaseConfigExampleFile()
     * @see #getRedmineDatabaseConfigFile()
     */
    void deployDatabase(Domain domain, RedmineService service) {
        def exampleFile = redmineConfigFile domain, service, redmineDatabaseConfigExampleFile
        def file = redmineConfigFile domain, service, redmineDatabaseConfigFile
        file.isFile() == false ? FileUtils.copyFile(exampleFile, file) : false
        def conf = currentConfiguration file
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle DumperOptions.FlowStyle.BLOCK
        Yaml yaml = new Yaml(options);
        Map map = yaml.load conf
        map.production.adapter = service.database.provider
        map.production.database = service.database.database
        map.production.host = service.database.host
        map.production.username = service.database.user
        map.production.password = service.database.password
        map.production.encoding = service.database.encoding
        conf = databaseTemplate.getText "databaseHead", "service", service
        conf += yaml.dump map
        FileUtils.writeStringToFile file, conf, charset
        logg.databaseConfigDeployed this, file, conf
    }

    /**
     * Returns the <i>Redmine</i> database configuration file property, for
     * example {@code "config/database.yml".}
     *
     * <ul>
     * <li>profile property {@code "redmine_database_configuration_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRedmineDatabaseConfigFile() {
        profileProperty "redmine_database_configuration_file", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> database example configuration file
     * property, for example {@code "config/database.yml.example".}
     *
     * <ul>
     * <li>profile property {@code "redmine_database_configuration_example_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRedmineDatabaseConfigExampleFile() {
        profileProperty "redmine_database_configuration_example_file", redmineProperties
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Redmine_2_5_Config"
        this.databaseTemplate = templates.getResource "database"
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
