/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.roundcube.linux;

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.roundcube.api.RoundcubeDatabaseConfig
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService

/**
 * MySQL Roundcube.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeMysqlConfig implements RoundcubeDatabaseConfig {

    public static final String NAME = "mysql";

    private ApacheScript script;

    Templates roundcubeTemplates

    TemplateResource roundcubeConfigTemplate

    @Override
    void setupDatabase(RoundcubeService service) {
        def database
        // TODO Auto-generated method stub

    }

    @Override
    public String getDatabase() {
        return NAME;
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script;
        roundcubeTemplates = templatesFactory.create "Roundcube_0_9"
        roundcubeConfigTemplate = roundcubeTemplates.getResource "config"
    }

    @Override
    ApacheScript getScript() {
        return script;
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
