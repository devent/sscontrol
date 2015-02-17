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
package com.anrisoftware.sscontrol.scripts.unpack;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Unpacks the archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Unpack implements Callable<Unpack> {

    private static final String RMDIR_COMMAND_DEFAULT = "rmdir";

    private static final String RMDIR_COMMAND_KEY = "rmdirCommand";

    private static final String MV_COMMAND_DEFAULT = "mv";

    private static final String MV_COMMAND_KEY = "mvCommand";

    private static final String TGZ_EXTENSION = ".tgz";

    private static final String ZIP_TYPE = "zip";

    private static final String TGZ_TYPE = "tgz";

    private static final String ZIP_EXTENSION = ".zip";

    private static final String TAR_GZ_EXTENSION = ".tar.gz";

    private static final String FILE_KEY = "file";

    private static final String TYPE_KEY = "type";

    private static final String BASH_COMMAND_KEY = "bashCommand";

    private static final String BASH_COMMAND_DEFAULT = "bash";

    private static final String TAR_BZ2_EXTENSION = ".tar.bz2";

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    private final UnpackLogger log;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    /**
     * @see UnpackFactory#create(Map, Object, Threads)
     */
    @Inject
    Unpack(UnpackLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        log.file(args, parent);
        log.output(args, parent);
        log.commands(args, parent);
        log.override(args, parent);
        log.strip(args, parent);
        if (!args.containsKey(BASH_COMMAND_KEY)) {
            args.put(BASH_COMMAND_KEY, BASH_COMMAND_DEFAULT);
        }
        if (!args.containsKey(MV_COMMAND_KEY)) {
            args.put(MV_COMMAND_KEY, MV_COMMAND_DEFAULT);
        }
        if (!args.containsKey(RMDIR_COMMAND_KEY)) {
            args.put(RMDIR_COMMAND_KEY, RMDIR_COMMAND_DEFAULT);
        }
        setupType(args);
    }

    @SuppressWarnings("rawtypes")
    private void setupType(Map<String, Object> args) {
        String type = typeFile((File) args.get(FILE_KEY));
        args.put(TYPE_KEY, type);
        args.put("command", ((Map) args.get("commands")).get(type));
    }

    private String typeFile(File file) {
        if (haveExtension(file, TGZ_EXTENSION)) {
            return TGZ_TYPE;
        }
        if (haveExtension(file, TAR_GZ_EXTENSION)) {
            return TGZ_TYPE;
        }
        if (haveExtension(file, TAR_BZ2_EXTENSION)) {
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
    public Unpack call() throws Exception {
        Templates templates = templatesFactory.create("ScriptsUnixTemplates");
        TemplateResource templateResource = templates.getResource("unpack");
        scriptExecFactory.create(args, parent, threads, templateResource,
                "unix").call();
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
