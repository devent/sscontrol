/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.fail2ban.ufw

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.Section
import com.anrisoftware.globalpom.initfileparser.SectionFormatterFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.remote.openssh.fail2ban.linux.Fail2BanScript
import com.anrisoftware.sscontrol.remote.service.RemoteService

/**
 * Ufw firewall fail2ban script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UfwFail2BanScript implements Fail2BanScript {

    @Inject
    InitFileParserFactory initFileParserFactory

    @Inject
    DefaultInitFileAttributesFactory initFileAttributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates scriptTemplates

    /**
     * Resource create UFW fail2ban action.
     */
    TemplateResource ufwactionTemplate

    /**
     * Parent script.
     */
    Object script

    @Override
    void deployFail2banScript(RemoteService service) {
        createUfwAction()
        createUfwSection()
    }

    void createUfwAction() {
        if (fail2banUfwActionFile.isFile()) {
            return
        }
        def str = ufwactionTemplate.getText("properties", script)
        FileUtils.write fail2banUfwActionFile, str, charset
    }

    void createUfwSection() {
        if (!fail2banJailLocalConfigFile.isFile()) {
            FileUtils.copyFile fail2banJailConfigFile, fail2banJailLocalConfigFile
        }
        def attributes = initFileAttributesFactory.create()
        def parser = initFileParserFactory.create(fail2banJailLocalConfigFile, attributes)()
        def sections = parser.inject([]) { acc, val -> acc << val }
        def defaultSection = sections.find { Section section -> section.name == "DEFAULT" }
        def sshSection = sections.find { Section section -> section.name == "ssh" }
        def formatter = sectionFormatterFactory.create attributes
        sshSection.properties.setProperty "enabled", "true"
        sshSection.properties.setProperty "ignoreip", fail2banIgnoreAddresses.join(" ")
        sshSection.properties.setProperty "bantime", Long.toString(fail2banBanTime.standardSeconds)
        sshSection.properties.setProperty "maxretry", Integer.toString(fail2banMaxRetries)
        sshSection.properties.setProperty "backend", fail2banBackend
        sshSection.properties.setProperty "destemail", fail2banDestinationEmail
        sshSection.properties.setProperty "banaction", fail2banBanAction
        def builder = new StringBuilder()
        formatter.format defaultSection, builder
        sections.findAll { Section section -> section.properties.getProperty("enabled") == "true" }.each {
            builder.append attributes.newLine
            formatter.format it, builder
        }
        FileUtils.write fail2banJailLocalConfigFile, builder.toString(), charset
    }

    @Override
    void setScript(Object script) {
        this.script = script
        this.scriptTemplates = templatesFactory.create "UfwFail2BanScript"
        this.ufwactionTemplate = scriptTemplates.getResource "ufwaction"
    }

    @Override
    Object getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
