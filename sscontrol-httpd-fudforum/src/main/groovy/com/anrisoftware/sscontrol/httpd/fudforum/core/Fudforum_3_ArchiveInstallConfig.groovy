/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04.FudforumArchivePropertiesProvider
import com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfig

/**
 * Installs <i>FUDForum 3</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fudforum_3_ArchiveInstallConfig extends FudforumArchiveInstallConfig {

    @Inject
    FudforumArchivePropertiesProvider propertiesProvider

    TemplateResource installScriptTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Fudforum_3_ArchiveInstallConfig"
        this.installScriptTemplate = templates.getResource "install_script"
    }

    @Override
    ContextProperties getFudforumProperties() {
        propertiesProvider.get()
    }

    @Override
    TemplateResource getInstallTemplate() {
        installScriptTemplate
    }
}
