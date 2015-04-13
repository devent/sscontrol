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

import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.install_packages_done;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.packages_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.runcommands.RunCommandsArg;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link InstallPackages}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class InstallPackagesLogger extends AbstractLogger {

    private static final String SYSTEM_ARG = "system";
    private static final String PACKAGES_ARG = "packages";
    private static final String COMMAND_ARG = "command";

    enum _ {

        command_null("Install packages command argument '%s' must be set"),

        packages_null("Packages argument '%s' must be set"),

        install_packages_done("Installed packages {} for {}, {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Inject
    private RunCommandsArg runCommandsArg;

    /**
     * Sets the context of the logger to {@link InstallPackages}.
     */
    public InstallPackagesLogger() {
        super(InstallPackages.class);
    }

    void installPackagesDone(Object parent, RunCommands runCommands,
            ProcessTask task, Map<String, Object> args) {
        debug(install_packages_done, args, parent, task);
        if (runCommands != null) {
            runCommands.add(args.get(COMMAND_ARG), args);
        }
    }

    RunCommands runCommands(Map<String, Object> args, Object parent) {
        return runCommandsArg.runCommands(args, parent);
    }

    void checkArgs(Map<String, Object> args) {
        notNull(args.get(COMMAND_ARG), command_null.toString(), COMMAND_ARG);
        notNull(args.get(PACKAGES_ARG), packages_null.toString(), PACKAGES_ARG);
        notNull(args.get(SYSTEM_ARG), packages_null.toString(), SYSTEM_ARG);
    }
}
