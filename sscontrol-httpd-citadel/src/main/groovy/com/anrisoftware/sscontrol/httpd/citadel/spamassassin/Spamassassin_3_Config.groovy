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
package com.anrisoftware.sscontrol.httpd.citadel.spamassassin

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService

/**
 * <i>Spamassassin 3.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Spamassassin_3_Config {

    /**
     * @see ServiceConfig#getScript()
     */
    private Object script

    @Inject
    ReportSafeModeAttributeRenderer reportSafeModeAttributeRenderer

    TemplateResource localConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Spamassassin_3_Config", ["renderers": [
                reportSafeModeAttributeRenderer
            ]]
        this.localConfigTemplate = templates.getResource "local_config"
    }

    /**
     * Deploys the <i>Spamassassin</i> configuration.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @see #getSpamassassinDefaultsFile()
     * @see #getCitadelProperties()
     */
    void deploySpamassassinConfig(CitadelService service) {
        def configs = []
        configs << configToken(localConfigTemplate, "rewriteHeaderConfig", "header", rewriteHeader)
        configs << configToken(localConfigTemplate, "reportSafeConfig", "mode", reportSafeMode)
        configs << configToken(localConfigTemplate, "trustedNetworksConfig", "networks", trustedNetworks)
        configs << configToken(localConfigTemplate, "requiredScoreConfig", "score", requiredScore)
        def file = spamassassinLocalConfFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    /**
     * Returns the list of needed packages for <i>Spamassassin</i> service, for
     * example {@code "spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_packages"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    List getSpamassassinPackages() {
        profileListProperty "spamassassin_packages", citadelProperties
    }

    /**
     * Returns the <i>Spamassassin</i> restart command, for
     * example {@code "/etc/init.d/spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_restart_command"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getSpamassassinRestartCommand() {
        profileProperty "spamassassin_restart_command", citadelProperties
    }

    /**
     * Returns the <i>Spamassassin</i> restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_restart_flags"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getSpamassassinRestartFlags() {
        profileProperty "spamassassin_restart_flags", citadelProperties
    }

    /**
     * Returns the <i>Spamassassin</i> local configuration file, for
     * example {@code "/etc/spamassassin/local.cf"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_local_conf_file"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    File getSpamassassinLocalConfFile() {
        profileProperty("spamassassin_local_conf_file", citadelProperties) as File
    }

    /**
     * Returns the <i>Spamassassin</i> rewrite header, for
     * example {@code "Subject *SPAM*"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_rewrite_header"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getRewriteHeader() {
        profileProperty "spamassassin_rewrite_header", citadelProperties
    }

    /**
     * Returns how <i>Spamassassin</i> should report spam in safe mode, for
     * example {@code "mimeAttachement"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_rewrite_header"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    ReportSafeMode getReportSafeMode() {
        def value = profileProperty "spamassassin_report_safe_mode", citadelProperties
        ReportSafeMode.valueOf value
    }

    /**
     * Returns <i>Spamassassin</i> trusted networks, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_trusted_networks"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    List getTrustedNetworks() {
        profileListProperty "spamassassin_trusted_networks", citadelProperties
    }

    /**
     * Returns <i>Spamassassin</i> required score for spam, for
     * example {@code "5.0"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_required_score"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    float getRequiredScore() {
        profileNumberProperty "spamassassin_required_score", citadelProperties
    }

    TokenTemplate configToken(TemplateResource template, Object... args) {
        def search = template.getText(true, "${args[0]}Search")
        def replace = template.getText(true, args)
        new TokenTemplate(search, replace)
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
