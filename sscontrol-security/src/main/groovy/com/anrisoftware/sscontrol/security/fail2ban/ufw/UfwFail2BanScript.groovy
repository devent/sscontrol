/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ufw

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributes
import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.Section
import com.anrisoftware.globalpom.initfileparser.SectionFactory
import com.anrisoftware.globalpom.initfileparser.SectionFormatterFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.security.fail2ban.linux.Fail2BanFirewall
import com.anrisoftware.sscontrol.security.services.Service
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Ufw firewall fail2ban script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UfwFail2BanScript implements Fail2BanFirewall {

    @Inject
    private UfwFail2BanScriptLogger log

    @Inject
    InitFileParserFactory initFileParserFactory

    @Inject
    DefaultInitFileAttributesFactory initFileAttributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    @Inject
    SectionFactory sectionFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates scriptTemplates

    /**
     * Resource create UFW fail2ban action.
     */
    TemplateResource ufwactionTemplate

    /**
     * Resource create {@code rsyslog} configuration.
     */
    TemplateResource rsyslogTemplate

    /**
     * Parent script.
     */
    LinuxScript script

    @Override
    void beforeConfiguration() {
        enableFirewall()
        deployRsyslogConfiguration()
        restartRsyslog()
    }

    @Override
    void deployFirewallScript(Service service) {
        createActionFile service
        createUfwSection service
    }

    /**
     * Enables the Firewall.
     */
    void enableFirewall() {
        def worker = script.execCommandFactory.create("$ufwCommand enable")()
        log.enabledFirewall script, worker
    }

    /**
     * Creates the UFW action file.
     *
     * @see #getUfwActionFile()
     */
    void createActionFile(Service service) {
        def file = ufwActionFile service
        def str = ufwactionTemplate.getText(true, "properties", script, "service", service)
        FileUtils.write file, str, charset
        log.actionFileCreated script, service, file, str
    }

    /**
     * Returns the fail2ban UFW action file, for
     * example {@code "action.d/ufw-<service.name>.conf".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "action_file_pattern"}</li>
     * </ul>
     *
     * @param service
     *            the {@link Service} service.
     *
     * @see #getDefaultProperties()
     */
    File ufwActionFile(Service service) {
        def pattern = profileProperty "action_file_pattern", defaultProperties
        def st = new ST(pattern).add("service", service).render()
        new File(configurationDir, st)
    }

    /**
     * Create UFW section in jail-local file.
     *
     * @param service
     *            the {@link Service} service.
     */
    void createUfwSection(Service service) {
        def attributes = initFileAttributesFactory.create()
        def parser = initFileParserFactory.create(jailLocalConfigFile, attributes)()
        def sections = parser.inject([]) { acc, val -> acc << val }
        def section = sections.find { Section section -> section.name == service.name }
        sections = setupsSection(section, service, sections)
        def builder = formatSection(attributes, sections)
        FileUtils.write jailLocalConfigFile, builder.toString(), charset
    }

    private setupsSection(Section section, Service service, sections) {
        if (section == null) {
            section = sectionFactory.create(service.name, new Properties())
            sections << section
        }
        section.with {
            properties.setProperty "enabled", "true"
            service.ignoring.addresses.size() > 0 ? properties.setProperty("ignoreip", service.ignoring.addresses.join(" ")) : false
            service.banning.banningTime != null ? properties.setProperty("bantime", Long.toString(service.banning.banningTime.standardSeconds)) : false
            service.banning.maxRetries != null ? properties.setProperty("maxretry", Integer.toString(service.banning.maxRetries)) : false
            service.banning.backend != null ? properties.setProperty("backend", service.banning.backend.toString()) : false
            service.notifyAddress != null ? properties.setProperty("destemail", service.notifyAddress) : false
            properties.setProperty "banaction", "ufw-${service.name}"
            properties.containsKey("filter") ? false : properties.setProperty("filter", filterFor(service.name))
            properties.containsKey("port") ? false : properties.setProperty("port", portsFor(service.name))
            properties.containsKey("logpath") ? false : properties.setProperty("logpath", logpathFor(service.name))
            haveFilter(service.name) ? properties.setProperty("filter", filterFor(service.name)) : false
            havePorts(service.name) ? properties.setProperty("port", portsFor(service.name)) : false
            haveLogpath(service.name) ? properties.setProperty("logpath", logpathFor(service.name)) : false
        }
        return sections
    }

    private StringBuilder formatSection(DefaultInitFileAttributes attributes, def sections) {
        def builder = new StringBuilder()
        def formatter = sectionFormatterFactory.create attributes
        sections.each {
            formatter.format it, builder
            builder.append attributes.newLine
        }
        return builder
    }

    /**
     * Returns contains the filter name for the service.
     *
     * @param name
     *            the service name.
     *
     * @return {@code true} that the profile property was set.
     *
     * @see #getFirewallProperties()
     */
    boolean haveFilter(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        containsKey "filter_${name}"
    }

    /**
     * Returns the filter name for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the filter name.
     *
     * @see #getFirewallProperties()
     */
    String filterFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        profileProperty "filter_${name}", firewallProperties
    }

    /**
     * Returns contains the ports name for the service.
     *
     * @param name
     *            the service name.
     *
     * @return {@code true} that the profile property was set.
     *
     * @see #getFirewallProperties()
     */
    boolean havePorts(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        containsKey "ports_${name}"
    }

    /**
     * Returns the ports for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the ports.
     *
     * @see #getFirewallProperties()
     */
    String portsFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        profileListProperty("ports_${name}", firewallProperties).join(",")
    }

    /**
     * Returns contains the logging path for the service.
     *
     * @param name
     *            the service name.
     *
     * @return {@code true} that the profile property was set.
     *
     * @see #getFirewallProperties()
     */
    boolean haveLogpath(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        containsKey "logpath_${name}"
    }

    /**
     * Returns the logging path for the service.
     *
     * @param name
     *            the service name.
     *
     * @return the logging path.
     *
     * @see #getFirewallProperties()
     */
    String logpathFor(String name) {
        name = StringUtils.replaceChars(name, "-", "_")
        profileProperty "logpath_${name}", firewallProperties
    }

    /**
     * Returns the Ufw command, for example {@code "/usr/sbin/ufw".}
     *
     * <ul>
     * <li>profile property {@code "ufw_command"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    String getUfwCommand() {
        profileProperty "ufw_command", firewallProperties
    }

    /**
     * Deploys the {@code rsyslog} configuration.
     */
    void deployRsyslogConfiguration() {
        def file = rsyslogConfFile
        if (file.isFile()) {
            def config = currentConfiguration file
            deployConfiguration configurationTokens(), config, rsyslogConfigurations(), file
        }
    }

    /**
     * Returns the {@code rsyslog} configurations.
     */
    def rsyslogConfigurations() {
        [
            repeatedMsgReduction(),
        ]
    }

    /**
     * Configures the {@code RepeatedMsgReduction} option.
     */
    def repeatedMsgReduction() {
        def search = rsyslogTemplate.getText(true, "repeatedMsgReductionConfig_search")
        def replace = rsyslogTemplate.getText(true, "repeatedMsgReductionConfig", "enabled", false)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the {@code rsyslog} configuration file, for
     * example {@code "/etc/rsyslog.conf".}
     *
     * <ul>
     * <li>profile property {@code "rsyslog_configuration_file"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    File getRsyslogConfFile() {
        profileDirProperty "rsyslog_configuration_file", firewallProperties
    }

    /**
     * Restart {@code rsyslog} service.
     */
    void restartRsyslog() {
        restartServices restartCommand: rsyslogRestartCommand, services: []
    }

    /**
     * Returns the {@code rsyslog} service restart command, for
     * example {@code "/sbin/restart rsyslog".}
     *
     * <ul>
     * <li>profile property {@code "rsyslog_restart_command"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    String getRsyslogRestartCommand() {
        profileDirProperty "rsyslog_restart_command", firewallProperties
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
        this.scriptTemplates = templatesFactory.create "UfwFail2BanScript"
        this.rsyslogTemplate = scriptTemplates.getResource "rsyslog"
        this.ufwactionTemplate = scriptTemplates.getResource "ufwaction"
    }

    @Override
    LinuxScript getScript() {
        script
    }

    /**
     * Returns the default Firewall properties.
     */
    abstract ContextProperties getFirewallProperties()

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
