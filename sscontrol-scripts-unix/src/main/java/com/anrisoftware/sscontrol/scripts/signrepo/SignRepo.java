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
package com.anrisoftware.sscontrol.scripts.signrepo;

import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger.KEY_KEY;
import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger.NAME_KEY;
import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger.SYSTEM_KEY;
import static com.anrisoftware.sscontrol.scripts.signrepo.SignRepoLogger.TMP_KEY;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Signs the repository.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SignRepo implements Callable<SignRepo> {

    private final SignRepoLogger log;

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    private TemplateResource templateResource;

    /**
     * @see SignRepoFactory#create(Map, Object, Threads)
     */
    @Inject
    SignRepo(SignRepoLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        log.command(args, parent);
        log.key(args, parent);
        log.system(args, parent);
        log.tmp(args, parent);
        log.name(args, parent);
    }

    @Override
    public SignRepo call() throws Exception {
        File file = copyKey(args);
        Map<String, Object> args = new HashMap<String, Object>(this.args);
        args.put(KEY_KEY, file);
        if (!checkRepo(args)) {
            signRepo(args);
            log.repoSigned(parent, args);
        }
        file.delete();
        return this;
    }

    private boolean checkRepo(Map<String, Object> args) throws Exception {
        Map<String, Object> taskargs = new HashMap<String, Object>(args);
        taskargs.put("outString", true);
        String name = args.get(NAME_KEY).toString();
        String system = args.get(SYSTEM_KEY).toString();
        String templateName = format("%s%s", "list", capitalize(system));
        ProcessTask task = scriptExecFactory.create(taskargs, parent, threads,
                templateResource, templateName).call();
        for (String string : split(task.getOut(), "\n")) {
            if (StringUtils.contains(string, name)) {
                return true;
            }
        }
        return false;
    }

    private ProcessTask signRepo(Map<String, Object> args) throws Exception {
        String system = args.get(SYSTEM_KEY).toString();
        String name = format("%s%s", "sign", capitalize(system));
        return scriptExecFactory.create(args, parent, threads,
                templateResource, name).call();
    }

    private File copyKey(Map<String, Object> args) throws IOException {
        File tmp = getArg(TMP_KEY);
        File keyfile = File.createTempFile("signing", "key", tmp);
        URL resource = getArg(KEY_KEY);
        copyURLToFile(resource, keyfile);
        return keyfile;
    }

    @SuppressWarnings("unchecked")
    private <T> T getArg(String key) {
        return (T) args.get(key);
    }

    @Inject
    public final void setTemplatesFactory(TemplatesFactory factory) {
        Templates templates = factory.create("ScriptsUnixTemplates");
        this.templateResource = templates.getResource("signrepo");
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
