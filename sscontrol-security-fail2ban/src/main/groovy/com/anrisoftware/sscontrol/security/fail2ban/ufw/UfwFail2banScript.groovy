/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ufw

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributes
import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.Section
import com.anrisoftware.globalpom.initfileparser.SectionFactory
import com.anrisoftware.globalpom.initfileparser.SectionFormatterFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.security.fail2ban.Fail2banService
import com.anrisoftware.sscontrol.security.fail2ban.Jail
import com.anrisoftware.sscontrol.security.fail2ban.linux.Fail2BanFirewallConfig

/**
 * <i>Ufw Fail2ban</i> firewall script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UfwFail2banScript implements Fail2BanFirewallConfig {

    @Inject
    private UfwFail2BanScriptLogger logg

    @Inject
    InitFileParserFactory initFileParserFactory

    @Inject
    DefaultInitFileAttributesFactory initFileAttributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    @Inject
    SectionFactory sectionFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    /**
     * Resource enable the firewall.
     */
    TemplateResource ufwenableTemplate

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
    Object script

    @Override
    void beforeConfiguration(Fail2banService service) {
        enableFirewall()
        deployRsyslogConfiguration()
        restartRsyslog()
    }

    @Override
    void deployFirewallScript(Fail2banService service, Jail jail) {
        createActionFile jail
        createUfwSection jail
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "UfwFail2BanScript"
        this.rsyslogTemplate = templates.getResource "rsyslog"
        this.ufwactionTemplate = templates.getResource "ufwaction"
        this.ufwenableTemplate = templates.getResource "ufwenable"
    }

    /**
     * Enables the firewall.
     */
    void enableFirewall() {
        def task = scriptExecFactory.create(
                log: log,
                command: ufwCommand,
                this, threads, ufwenableTemplate, "ufwenable")()
        logg.enabledFirewall this, task
    }

    /**
     * Creates the <i>Ufw</i> action file.
     *
     * @param jail
     *            the {@link Jail} jail.
     *
     * @see #getUfwActionFile()
     */
    void createActionFile(Jail jail) {
        def file = ufwActionFile jail
        def str = ufwactionTemplate.getText(true, "properties", script, "jail", jail)
        FileUtils.write file, str, charset
        logg.actionFileCreated this, jail, file, str
    }

    /**
     * Returns the <i>Ufw</i> action file, for
     * example {@code "action.d/ufw-<service>.conf"} If the file path is
     * not absolute then the file is assumed to be under
     * the <i>Fail2ban</i> configuration directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_action_file_pattern"}</li>
     * </ul>
     *
     * @param jail
     *            the {@link Jail} jail.
     *
     * @see #getFail2banProperties()
     */
    File ufwActionFile(Jail jail) {
        def pattern = profileProperty "fail2ban_action_file_pattern", fail2banProperties
        def st = new ST(pattern).add("service", jail.service).render()
        new File(configurationDir, st)
    }

    /**
     * Create UFW section in jail-local file.
     *
     * @param jail
     *            the {@link Jail} jail.
     *
     * @see #getJailLocalConfigFile()
     */
    void createUfwSection(Jail jail) {
        def attributes = initFileAttributesFactory.create()
        attributes.stringQuoteEnabled = false
        def parser = initFileParserFactory.create(jailLocalConfigFile, attributes)()
        def sections = parser.inject([]) { acc, val -> acc << val }
        def section = sections.find { Section section -> section.name == jail.service }
        sections = setupsSection(section, jail, sections)
        def builder = formatSection(attributes, sections)
        FileUtils.write jailLocalConfigFile, builder.toString(), charset
    }

    private setupsSection(Section section, Jail jail, List sections) {
        if (section == null) {
            section = sectionFactory.create(jail.service, new Properties())
            sections << section
        }
        section.with {
            properties.setProperty "enabled", "true"
            properties.setProperty "banaction", "ufw-${jail.service}"
            if (jail.ignoreAddresses && jail.ignoreAddresses.size() > 0) {
                properties.setProperty "ignoreip", jail.ignoreAddresses.join(" ")
            }
            if (jail.banningTime) {
                properties.setProperty "bantime", Long.toString(jail.banningTime.getStandardSeconds())
            }
            if (jail.banningRetries) {
                properties.setProperty "maxretry", Integer.toString(jail.banningRetries)
            }
            if (jail.banningBackend) {
                properties.setProperty "backend", jail.banningBackend.toString()
            }
            if (jail.notify) {
                properties.setProperty "destemail", jail.notify
            }
            if (!properties.containsKey("filter")) {
                properties.setProperty "filter", filterFor(jail.service)
            }
            if (!properties.containsKey("port")) {
                properties.setProperty "port", portsFor(jail.service)
            }
            if (!properties.containsKey("logpath")) {
                properties.setProperty "logpath", logpathFor(jail.service)
            }
            if (haveFilter(jail.service)) {
                properties.setProperty "filter", filterFor(jail.service)
            }
            if (havePorts(jail.service)) {
                properties.setProperty "port", portsFor(jail.service)
            }
            if (haveLogpath(jail.service)) {
                properties.setProperty "logpath", logpathFor(jail.service)
            }
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
     * Restart the <i>rsyslog</i> service.
     *
     * @see #getRsyslogRestartCommand()
     * @see #getRsyslogRestartServices()
     * @see #getRsyslogRestartFlags()
     */
    void restartRsyslog() {
        restartServicesFactory.create(
                log: log,
                command: rsyslogRestartCommand,
                services: rsyslogRestartServices,
                flags: rsyslogRestartFlags,
                this, threads)()
    }

    /**
     * Returns the {@code rsyslog} service restart command, for
     * example {@code "/sbin/restart"}
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

    /**
     * Returns the {@code rsyslog} services to restart, for
     * example {@code "rsyslog"}
     *
     * <ul>
     * <li>profile property {@code "rsyslog_restart_services"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    List getRsyslogRestartServices() {
        profileListProperty "rsyslog_restart_services", firewallProperties
    }

    /**
     * Returns the {@code rsyslog} services to restart, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "rsyslog_restart_flags"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    String getRsyslogRestartFlags() {
        profileProperty "rsyslog_restart_flags", firewallProperties
    }

    @Override
    void setScript(Object script) {
        this.script = script
    }

    @Override
    Object getScript() {
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
