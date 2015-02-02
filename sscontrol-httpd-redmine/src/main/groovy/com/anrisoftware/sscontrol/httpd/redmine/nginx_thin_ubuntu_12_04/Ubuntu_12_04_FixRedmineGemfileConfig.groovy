/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService

/**
 * <i>Ubuntu 12.04 Redmine</i> fixes gems versions.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_FixRedmineGemfileConfig {

    private Object script

    private TemplateResource configTemplate

    @Inject
    private RedminePropertiesProvider propertiesProvider

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Ubuntu_12_04_FixRedmineGemfileConfig"
        this.configTemplate = templates.getResource "gemfile_fix_config"
    }

    /**
     * Deploys the gems fix.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void deployGemsFix(Domain domain, RedmineService service) {
        def file = redmineGemfileFile
        if (!file.absolute) {
            file = new File(redmineDir(domain, service), file.path)
        }
        def configs = [
            gemI18n(domain, service),
            gemRmagick(domain, service),
        ]
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    def gemRmagick(Domain domain, RedmineService service) {
        def search = configTemplate.getText(true, "gemRmagickConfigSearch")
        def replace = configTemplate.getText(true, "gemRmagickConfig", "version", rmagickVersion)
        new TokenTemplate(search, replace)
    }

    def gemI18n(Domain domain, RedmineService service) {
        def search = configTemplate.getText(true, "gemI18nConfigSearch")
        def replace = configTemplate.getText(true, "gemI18nConfig", "version", i18nVersion)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the <i>Redmine</i> gems file path, for
     * example {@code "Gemfile".}
     *
     * <ul>
     * <li>profile property {@code "redmine_redmine_gemfile_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    File getRedmineGemfileFile() {
        profileDirProperty "redmine_redmine_gemfile_file", redmineProperties
    }

    /**
     * Returns the <i>rmagick</i> gem version, for
     * example {@code "2.13.3"}.
     *
     * <ul>
     * <li>profile property {@code "redmine_rmagick_version"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getRmagickVersion() {
        profileProperty "redmine_rmagick_version", redmineProperties
    }

    /**
     * Returns the <i>i18n</i> gem version, for
     * example {@code "0.6.11"}.
     *
     * <ul>
     * <li>profile property {@code "redmine_i18n_version"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getI18nVersion() {
        profileProperty "redmine_i18n_version", redmineProperties
    }

    /**
     * Returns the <i>Redmine</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    ContextProperties getRedmineProperties() {
        propertiesProvider.get()
    }

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
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
