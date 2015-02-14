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

import java.util.regex.Matcher

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService

/**
 * <i>Redmine</i> tracking script configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Redmine_2_TrackingScriptConfig {

    private Object script

    @Inject
    Redmine_2_TrackingScriptConfigLogger logg

    @Inject
    MailConfigRepresenter mailConfigRepresenter

    TemplateResource trackingTemplate

    /**
     * Deploys the <i>Redmine</i> tracking script configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineBaseHtmlFile()
     */
    void deployTracking(Domain domain, RedmineService service) {
        if (!service.trackingScript) {
            return
        }
        def file = redmineBaseHtmlFile domain, service
        def script = IOUtils.toString service.trackingScript, charset
        def search = trackingTemplate.getText true, "trackingConfigSearch"
        def replace = Matcher.quoteReplacement trackingTemplate.getText(true, "trackingConfig", "script", script)
        def conf = FileUtils.readFileToString file, charset
        conf = conf.replaceAll search, replace
        FileUtils.write file, conf, charset
        logg.configDeployed this, file, conf
    }

    def trackingScriptConfig(RedmineService service) {
        def script = IOUtils.toString(service.trackingScript, charset)
        def search = trackingTemplate.getText(true, "trackingConfigSearch")
        def replace = trackingTemplate.getText(true, "trackingConfig", "script", script)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the <i>Redmine</i> base HTML file, for
     * example {@code "app/views/layouts/base.html.erb".}
     *
     * <ul>
     * <li>profile property {@code "redmine_base_html_file"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineProperties()
     */
    File redmineBaseHtmlFile(Domain domain, RedmineService service) {
        profileFileProperty "redmine_base_html_file", redmineDir(domain, service), redmineProperties
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Redmine_2_5_Config"
        this.trackingTemplate = templates.getResource "tracking_config"
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
