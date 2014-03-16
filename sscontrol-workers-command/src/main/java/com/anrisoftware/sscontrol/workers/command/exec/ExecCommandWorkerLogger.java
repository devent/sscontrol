/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.workers.command.exec;

import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.command;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.error_execute_command_message;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.exit_code;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.finish_command_debug;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.finish_command_info;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.finish_command_trace;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.loadTexts;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.start_command_trace;
import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerLogger._.worker_name;

import java.io.IOException;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ExecCommandWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecCommandWorkerLogger extends AbstractLogger {

    enum _ {

        worker_name,

        command,

        exit_code,

        out,

        err,

        error_execute_command_message,

        finish_command_trace,

        finish_command_debug,

        finish_command_info,

        start_command_trace;

        public static void loadTexts(TextsFactory factory) {
            String name = ExecCommandWorkerLogger.class.getSimpleName();
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
     * Create logger for {@link ExecCommandWorker}.
     */
    public ExecCommandWorkerLogger() {
        super(ExecCommandWorker.class);
    }

    @Inject
    void setTextsFactory(TextsFactory factory) {
        loadTexts(factory);
    }

    WorkerException errorExecuteCommand(ExecCommandWorker worker, IOException e) {
        String out = worker.getOut();
        String err = worker.getErr();
        return logException(
                new CommandException(e, worker.getCommand(),
                        worker.getExitCode(), out, err)
                        .add(worker_name, worker)
                        .add(command, worker.getCommand())
                        .add(exit_code, worker.getExitCode()).add(out, out)
                        .add(err, err), error_execute_command_message,
                worker.getCommand());
    }

    void finishedProcess(ExecCommandWorker worker) {
        int exitCode = worker.getExitCode();
        if (isTraceEnabled()) {
            String out = worker.getOut();
            String err = worker.getErr();
            trace(finish_command_trace, worker, exitCode, out, err);
        } else if (isDebugEnabled()) {
            debug(finish_command_debug, worker, exitCode);
        } else {
            info(finish_command_info, worker.getCommandName(), exitCode);
        }
    }

    void startProcess(ExecCommandWorker worker) {
        trace(start_command_trace, worker);
    }
}
