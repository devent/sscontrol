/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.unix;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;

@Singleton
public class CommandTemplatesProvider implements Provider<Templates> {

    private static final String TEMPLATES_NAME = "ScriptsUnixTemplates";

    @Inject
    private TemplatesFactory templatesFactory;

    private Templates commandTemplates;

    @Override
    public Templates get() {
        lazyCreateTemplates();
        return commandTemplates;
    }

    private void lazyCreateTemplates() {
        if (commandTemplates == null) {
            this.commandTemplates = templatesFactory.create(TEMPLATES_NAME);
        }
    }

}
