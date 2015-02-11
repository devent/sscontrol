/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.initfileparser.DefaultInitFileAttributesFactory
import com.anrisoftware.globalpom.initfileparser.InitFileAttributes
import com.anrisoftware.globalpom.initfileparser.InitFileParserFactory
import com.anrisoftware.globalpom.initfileparser.Section
import com.anrisoftware.globalpom.initfileparser.SectionFactory
import com.anrisoftware.globalpom.initfileparser.SectionFormatterFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory

/**
 * Parses the <i>Piwik</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ConfigParser {

    private Object parent

    @Inject
    ConfigParserLogger log

    @Inject
    InitFileParserFactory parserFactory

    @Inject
    DefaultInitFileAttributesFactory attributesFactory

    @Inject
    SectionFormatterFactory sectionFormatterFactory

    @Inject
    SectionFactory sectionFactory

    @Inject
    DebugLevelRenderer debugLevelRenderer

    @Inject
    ChangeFileModFactory changeFileModFactory

    TemplateResource piwikConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Piwik_2_Config"
        piwikConfigTemplate = templates.getResource "config"
    }

    /**
     * Deploys the <i>Piwik</i> configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void deployConfig(Domain domain, PiwikService service) {
        File file = piwikConfigFile domain, service
        if (file.exists()) {
            deployConfigIni(domain, service)
            secureConfigIni domain, service
        }
    }

    private secureConfigIni(Domain domain, PiwikService service) {
        File file = piwikConfigFile domain, service
        changeFileModFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "o-rw",
                files: file,
                this, threads)()
    }

    private deployConfigIni(Domain domain, PiwikService service) {
        def attributes = createIniAttributes()
        List sections = readSections domain, service, attributes
        Section databaseSection = sections.find({it.name == "database"})
        databaseSection = sectionFactory.create "database", databaseProperties(domain, service, databaseSection)
        Section debugSection = sections.find({it.name == "log"})
        debugSection = sectionFactory.create "log", debugProperties(domain, service, debugSection)
        replaceSection sections, databaseSection
        replaceSection sections, debugSection
        writeConfig domain, service, sections, attributes
    }

    private List readSections(Domain domain, PiwikService service, def attributes) {
        File file = piwikConfigFile domain, service
        List sections = []
        if (file.exists()) {
            sections = parseConfigFile file, attributes
        }
        return sections
    }

    private void writeConfig(Domain domain, PiwikService service, List sections, def attributes) {
        File file = piwikConfigFile domain, service
        def formatter = sectionFormatterFactory.create attributes
        def lines = []
        lines << piwikConfigTemplate.getText("configHeader")
        lines = sections.inject(lines) { list, section ->
            list.add ''
            list.addAll StringUtils.split(formatter.format(section), '\n')
            list
        }
        FileUtils.writeLines file, charset.name(), lines
        log.deployedConfiguration this, domain, file, lines
    }

    private List parseConfigFile(File file, def attributes) {
        def parser = parserFactory.create(file, attributes)()
        parser.inject([]) { acc, val -> acc << val }
    }

    private InitFileAttributes createIniAttributes() {
        def attributes = attributesFactory.create()
        attributes.comment = ';'
        return attributes
    }

    private databaseProperties(Domain domain, PiwikService service, Section section) {
        def properties
        if (section) {
            properties = section.properties
        } else {
            properties = new Properties()
        }
        if (service.database.database) {
            properties.dbname = service.database.database
        }
        if (service.database.user) {
            properties.username = service.database.user
        }
        if (service.database.password) {
            properties.password = service.database.password
        }
        if (service.database.host) {
            properties.host = service.database.host
        }
        if (service.database.port) {
            properties.port = service.database.port
        }
        if (service.database.prefix) {
            properties.tables_prefix = service.database.prefix
        }
        if (service.database.adapter) {
            properties.adapter = service.database.adapter
        }
        if (service.database.type) {
            properties.type = service.database.type
        }
        if (service.database.schema) {
            properties.schema = service.database.schema
        }
        properties
    }

    private debugProperties(Domain domain, PiwikService service, Section section) {
        def properties
        if (section) {
            properties = section.properties
        } else {
            properties = new Properties()
        }
        if (service.debugLogging("writer") && service.debugLogging("writer")["piwik"]) {
            properties.log_writers = [
                service.debugLogging("writer")["piwik"]
            ]
        }
        if (service.debugLogging("level") && service.debugLogging("level")["piwik"]) {
            properties.log_level = debugLevelRenderer.toString service.debugLogging("level")["piwik"]
        }
        if (service.debugLogging("file") && service.debugLogging("file")["piwik"]) {
            properties.logger_file_path = service.debugLogging("file")["piwik"]
        }
        properties
    }

    private List replaceSection(List sections, Section section) {
        int index = findSection(sections, section)
        if (index != -1) {
            sections.set index, section
        } else {
            sections.add section
        }
        return sections
    }

    private int findSection(List sections, Section section) {
        int index = -1
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).name == section.name) {
                index = i
                break
            }
        }
        return index
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        parent.getServiceName()
    }

    /**
     * Sets the parent script.
     */
    void setScript(Object parent) {
        this.parent = parent
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        parent
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        parent.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this).append(parent.toString()).toString();
    }
}
