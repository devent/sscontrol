/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.importdb;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Imports database tables and data.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ImportDatabase implements Callable<ImportDatabase> {

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    private final String databaseDriver;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    private TemplateResource importDatabaseTemplate;

    /**
     * @see ImportDatabaseFactory#create(Map, Object, Threads)
     */
    @Inject
    ImportDatabase(ImportDatabaseLogger log,
            @Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        this.databaseDriver = log.databaseDriver(args, parent);
        log.command(args, parent);
        log.user(args, parent);
        log.password(args, parent);
        log.database(args, parent);
        log.scriptFile(args, parent);
        log.drop(args, parent);
    }

    @Inject
    public final void setTemplatesFactory(TemplatesFactory factory) {
        Templates templates = factory.create("ScriptsUnixTemplates");
        this.importDatabaseTemplate = templates.getResource("importDatabase");
    }

    @Override
    public ImportDatabase call() throws Exception {
        scriptExecFactory.create(args, parent, threads, importDatabaseTemplate,
                databaseDriver).call();
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
