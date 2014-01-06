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

import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.error_copy;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.error_copy_message;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.error_processing;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.error_processing_message;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.finish_script_debug;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.finish_script_info;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.finish_script_trace;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.loadTexts;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.start_script_trace;
import static com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerLogger._.worker_name;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.api.ResourcesException;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ScriptCommandWorkerLogger}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptCommandWorkerLogger extends AbstractLogger {

    enum _ {

        error_processing,

        worker_name,

        error_processing_message,

        error_copy,

        error_copy_message,

        finish_script_trace,

        finish_script_debug,

        finish_script_info,

        start_script_trace;

        public static void loadTexts(TextsFactory factory) {
            String name = ScriptCommandWorkerLogger.class.getSimpleName();
            Texts texts = factory.create(name);
            for (_ value : values()) {
                value.text = texts.getResource(value.name()).getText();
            }
        }

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Create logger for {@link ScriptCommandWorker}.
     */
    public ScriptCommandWorkerLogger() {
        super(ScriptCommandWorker.class);
    }

    @Inject
    void setTextsFactory(TextsFactory factory) {
        loadTexts(factory);
    }

    WorkerException errorProcessTemplate(ScriptCommandWorker worker,
            ResourcesException e) {
        return logException(new WorkerException(error_processing, e).add(
                worker_name, worker), error_processing_message, worker
                .getTemplate().getName());
    }

    WorkerException errorCopyScript(ScriptCommandWorker worker, IOException e) {
        return logException(
                new WorkerException(error_copy, e).add(worker_name, worker),
                error_copy_message, worker.getTemplate().getName());
    }

    void finishedScript(ScriptCommandWorker worker) {
        int exitCode = worker.getExitCode();
        if (isTraceEnabled()) {
            String out = worker.getOut();
            String err = worker.getErr();
            trace(finish_script_trace, worker, exitCode, worker.getScript(),
                    out, err);
        } else if (isDebugEnabled()) {
            debug(finish_script_debug, worker, exitCode);
        } else {
            info(finish_script_info, worker.getCommandName(), exitCode);
        }
    }

    void startScript(ScriptCommandWorker worker) {
        trace(start_script_trace, worker, worker.getScript());
    }
}
