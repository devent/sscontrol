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
package com.anrisoftware.sscontrol.scripts.pack;

import java.io.File;
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
 * Archive from specified files.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Pack implements Callable<Pack> {

    private static final String UNIX_TEMPLATE = "unix";

    private static final String TEMPLATES = "ScriptsUnixTemplates";

    private static final String PACK_TEMPLATE = "pack";

    private static final String COMMANDS_KEY = "commands";

    private static final String COMMAND_KEY = "command";

    private static final String TGZ_EXTENSION = ".tgz";

    private static final String ZIP_TYPE = "zip";

    private static final String TGZ_TYPE = "tgz";

    private static final String ZIP_EXTENSION = ".zip";

    private static final String TAR_GZ_EXTENSION = ".tar.gz";

    private static final String OUTPUT_KEY = "output";

    private static final String TYPE_KEY = "type";

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    private final PackLogger log;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    /**
     * @see PackFactory#create(Map, Object, Threads)
     */
    @Inject
    Pack(PackLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        log.files(args, parent);
        log.output(args, parent);
        log.commands(args, parent);
        setupType(args);
    }

    @SuppressWarnings("rawtypes")
    private void setupType(Map<String, Object> args) {
        String type = typeFile((File) args.get(OUTPUT_KEY));
        args.put(TYPE_KEY, type);
        args.put(COMMAND_KEY, ((Map) args.get(COMMANDS_KEY)).get(type));
    }

    private String typeFile(File file) {
        if (haveExtension(file, TGZ_EXTENSION)) {
            return TGZ_TYPE;
        }
        if (haveExtension(file, TAR_GZ_EXTENSION)) {
            return TGZ_TYPE;
        }
        if (haveExtension(file, ZIP_EXTENSION)) {
            return ZIP_TYPE;
        }
        throw log.unknownType(parent, file);
    }

    private boolean haveExtension(File file, String extension) {
        return file.getName().endsWith(extension);
    }

    @Override
    public Pack call() throws Exception {
        Templates templates = templatesFactory.create(TEMPLATES);
        TemplateResource templateResource = templates
                .getResource(PACK_TEMPLATE);
        scriptExecFactory.create(args, parent, threads, templateResource,
                UNIX_TEMPLATE).call();
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
