package com.anrisoftware.sscontrol.scripts.unix;

import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.log_null;
import static com.anrisoftware.sscontrol.scripts.unix.InstallPackagesLogger._.packages_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link InstallPackages}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class InstallPackagesLogger extends AbstractLogger {

    private static final String LOG_ARG = "log";
    private static final String PACKAGES_ARG = "packages";
    private static final String COMMAND_ARG = "command";

    enum _ {

        command_null("Install packages command argument '%s' must be set"),

        packages_null("Packages argument '%s' must be set"),

        log_null("Logger argument '%s' must be set"),

        install_packages_done_trace("Installed packages {} for {}, {}."),

        install_packages_done_debug("Installed packages {} for {}."),

        install_packages_done_info("Installed service packages for script {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link InstallPackages}.
     */
    public InstallPackagesLogger() {
        super(InstallPackages.class);
    }

    void installPackagesDone(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, args, parent);
        } else {
            info(install_packages_done_info, parent);
        }
    }

    void checkArgs(Map<String, Object> args) {
        notNull(args.get(COMMAND_ARG), command_null.toString(), COMMAND_ARG);
        notNull(args.get(PACKAGES_ARG), packages_null.toString(), PACKAGES_ARG);
        notNull(args.get(LOG_ARG), log_null.toString(), LOG_ARG);
    }
}
