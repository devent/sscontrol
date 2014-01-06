/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.command.script;

import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker.DEFAULT_TIMEOUT_MS;
import static java.io.File.createTempFile;
import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.api.ResourcesException;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Execute a shell script. The shell script is created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ScriptCommandWorker implements Worker {

    private static final String WORKER = "worker";

    private static final String SCRIPT = "script";

    private static final String ATTRIBUTES = "attributes";

    private static final String TEMPLATE = "template";

    private final TemplateResource template;

    private final Map<String, String> environment;

    private final Object[] attributes;

    @Inject
    private transient ExecCommandWorkerFactory executeFactory;

    @Inject
    private ScriptCommandWorkerLogger log;

    private ExecCommandWorker commandWorker;

    private String shell;

    private String scriptString;

    private File scriptFile;

    private boolean valuesSet;

    private int[] values;

    private long timeoutMs;

    /**
     * @see ScriptCommandWorkerFactory#create(TemplateResource, Object...)
     */
    @AssistedInject
    ScriptCommandWorker(@Assisted TemplateResource template,
            @Assisted Object... attributes) {
        this(template, null, attributes);
    }

    /**
     * @see ScriptCommandWorkerFactory#create(TemplateResource, Map, Object...)
     */
    @AssistedInject
    ScriptCommandWorker(@Assisted TemplateResource template,
            @Assisted Map<String, String> environment,
            @Assisted Object... attributes) {
        this(template, environment, DEFAULT_TIMEOUT_MS, attributes);
    }

    /**
     * @see ScriptCommandWorkerFactory#create(TemplateResource, Map, long,
     *      Object...)
     */
    @AssistedInject
    ScriptCommandWorker(@Assisted TemplateResource template,
            @Assisted Map<String, String> environment,
            @Assisted long timeoutMs, @Assisted Object... attributes) {
        this.template = template;
        this.environment = environment;
        this.timeoutMs = timeoutMs;
        this.attributes = attributes;
        this.valuesSet = false;
        this.values = null;
    }

    /**
     * Sets the script command worker properties.
     * 
     * @param provider
     *            the {@link PropertiesProvider}.
     */
    @Inject
    void setProperties(PropertiesProvider provider) {
        ContextProperties p = provider.get();
        this.shell = p.getProperty("shell");
    }

    @Override
    public Worker call() throws WorkerException {
        scriptString = createScript(template, attributes);
        scriptFile = copyScript();
        try {
            commandWorker = createCommandWorker();
            log.startScript(this);
            commandWorker.call();
            log.finishedScript(this);
        } finally {
            scriptFile.delete();
        }
        return this;
    }

    private ExecCommandWorker createCommandWorker() throws WorkerException {
        ExecCommandWorker worker = executeFactory.create(
                format("%s %s", shell, scriptFile.getAbsolutePath()),
                environment, timeoutMs);
        worker.setQuotation(false);
        if (valuesSet) {
            worker.setExitValues(values);
        }
        worker.setTimeoutMs(timeoutMs);
        return worker;
    }

    private File copyScript() throws WorkerException {
        try {
            File file = createTempFile(SCRIPT, ".sh");
            FileUtils.write(file, scriptString);
            return file;
        } catch (IOException e) {
            throw log.errorCopyScript(this, e);
        }
    }

    private String createScript(TemplateResource template, Object[] attributes)
            throws WorkerException {
        try {
            return template.getText(true, attributes);
        } catch (ResourcesException e) {
            throw log.errorProcessTemplate(this, e);
        }
    }

    public TemplateResource getTemplate() {
        return template;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public void setExitValue(int value) {
        setExitValues(new int[] { value });
    }

    /**
     * Sets the list of valid exit values for the process.
     * 
     * @param values
     *            the integer array of exit values or {@code null} to skip
     *            checking of exit codes.
     * 
     * @see Executor#setExitValues(int[])
     */
    public void setExitValues(int[] values) {
        this.valuesSet = true;
        this.values = values;
    }

    /**
     * Sets to skip the checking of the exit codes.
     * 
     * @param skip
     *            set to {@code true} to skip the checking.
     */
    public void setSkipExitValue(boolean skip) {
        if (skip) {
            setExitValues(null);
        }
    }

    public int getExitCode() {
        return commandWorker.getExitCode();
    }

    public String getOut() {
        return commandWorker.getOut();
    }

    public String getOut(String charsetName)
            throws UnsupportedEncodingException {
        return commandWorker.getOut(charsetName);
    }

    public String getErr() {
        return commandWorker.getErr();
    }

    public String getErr(String charsetName)
            throws UnsupportedEncodingException {
        return commandWorker.getErr(charsetName);
    }

    public String getCommand() {
        return scriptString;
    }

    public String getCommandName() {
        return commandWorker.getCommandName();
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public String getScript() {
        return scriptString;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder = commandWorker != null ? builder.append(WORKER, commandWorker)
                : builder;
        builder.append(TEMPLATE, template);
        builder.append(ATTRIBUTES, Arrays.toString(attributes));
        builder = scriptFile != null ? builder.append(SCRIPT, scriptFile)
                : builder;
        return builder.toString();
    }

}
