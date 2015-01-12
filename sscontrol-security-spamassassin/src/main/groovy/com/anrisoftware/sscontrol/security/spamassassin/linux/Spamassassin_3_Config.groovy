/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.linux

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.security.spamassassin.ClearType
import com.anrisoftware.sscontrol.security.spamassassin.ReportSafeMode
import com.anrisoftware.sscontrol.security.spamassassin.RewriteType
import com.anrisoftware.sscontrol.security.spamassassin.SpamassassinService

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
    Spamassassin_3_ConfigLogger logg

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
     * Setups the default debug logging.
     *
     * @param service
     *            the {@link SpamassassinService} service.
     *
     * @see #getDefaultDebugLogLevel()
     * @see #getDefaultDebugLogTarget()
     */
    void setupDefaultDebug(SpamassassinService service) {
        def logLevel = defaultDebugLogLevel
        if (!service.debugLogging("level") || !service.debugLogging("level")["log"]) {
            service.debug "log", level: logLevel
        }
    }

    /**
     * Setups the default properties of the service.
     *
     * @param service
     *            the {@link SpamassassinService} service.
     *
     * @see #getDefaultClearHeaders()
     * @see #getDefaultRewriteHeaderSubject()
     * @see #getDefaultTrustedNetworks()
     * @see #getDefaultSpamScore()
     */
    void setupDefaults(SpamassassinService service) {
        if (!service.clearHeaders) {
            service.clear defaultClearHeaders
        }
        if (!service.rewriteHeaders || !service.rewriteHeaders["subject"]) {
            service.rewrite RewriteType.subject, header: defaultRewriteHeaderSubject
        }
        if (!service.addHeaders) {
        }
        if (!service.trustedNetworks) {
            service.trusted networks: defaultTrustedNetworks
        }
        if (!service.spamScore) {
            service.spam score: defaultSpamScore
        }
    }

    /**
     * Deploys the <i>Spamassassin</i> configuration.
     *
     * @param service
     *            the {@link SpamassassinService}.
     *
     * @see #getSpamassassinLocalConfFile()
     * @see #getReportSafeMode()
     */
    void deploySpamassassinConfig(SpamassassinService service) {
        def configs = []
        configs << localConfigTemplate.getText(true, "configHeader")
        configs << localConfigTemplate.getText(true, "clearHeadersConfig", "clear", service.clearHeaders == ClearType.headers)
        configs << localConfigTemplate.getText(true, "rewriteHeaderSubjectConfig", "rewrite", service.rewriteHeaders["subject"])
        service.addHeaders.each {
            configs << localConfigTemplate.getText(true, "addHeaderConfig", "header", it)
        }
        configs << localConfigTemplate.getText(true, "reportSafeConfig", "mode", reportSafeMode)
        configs << localConfigTemplate.getText(true, "trustedNetworksConfig", "networks", service.trustedNetworks)
        configs << localConfigTemplate.getText(true, "requiredScoreConfig", "score", service.spamScore)
        FileUtils.writeLines spamassassinConfFile, charset.name(), configs
        logg.deploySpamassassinConfig this, spamassassinConfFile, configs
    }

    /**
     * Returns the <i>Spamassassin</i> configuration file, for
     * example {@code "/etc/spamassassin/99_robobee.cf"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_conf_file"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    File getSpamassassinConfFile() {
        profileDirProperty "spamassassin_conf_file", spamassassinProperties
    }

    /**
     * Returns the default debug log level, for
     * example {@code 0}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_default_debug_log_level"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    int getDefaultDebugLogLevel() {
        profileNumberProperty "spamassassin_default_debug_log_level", spamassassinProperties
    }

    /**
     * Returns the default clear headers, for
     * example {@code "none"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_default_clear_headers"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    ClearType getDefaultClearHeaders() {
        def value = profileProperty "spamassassin_default_clear_headers", spamassassinProperties
        ClearType.valueOf value
    }

    /**
     * Returns the default rewrite subject header, for
     * example {@code "*SPAM*"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_default_rewrite_header_subject"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    String getDefaultRewriteHeaderSubject() {
        profileProperty "spamassassin_default_rewrite_header_subject", spamassassinProperties
    }

    /**
     * Returns the default trusted networks, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_default_trusted_networks"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    List getDefaultTrustedNetworks() {
        profileListProperty "spamassassin_default_trusted_networks", spamassassinProperties
    }

    /**
     * Returns the default required score for spam, for
     * example {@code "5.0"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_default_spam_score"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    double getDefaultSpamScore() {
        profileNumberProperty "spamassassin_default_spam_score", spamassassinProperties
    }

    /**
     * Returns how should report spam in safe mode, for
     * example {@code "mimeAttachement"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_report_safe_mode"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    ReportSafeMode getReportSafeMode() {
        def value = profileProperty "spamassassin_report_safe_mode", spamassassinProperties
        ReportSafeMode.valueOf value
    }

    final TokenTemplate configToken(TemplateResource template, Object... args) {
        def search = template.getText(true, "${args[0]}Search")
        def replace = template.getText(true, args)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the default <i>Spamassassin</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getSpamassassinProperties()

    /**
     * Returns the <i>Spamassassin</i> service name.
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
