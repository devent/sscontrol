package com.anrisoftware.sscontrol.scripts.unix;

import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.command_null;
import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.log_null;
import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.restarted_service_debug;
import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.restarted_service_info;
import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.restarted_service_trace;
import static com.anrisoftware.sscontrol.scripts.unix.RestartServicesLogger._.services_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RestartServices}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RestartServicesLogger extends AbstractLogger {

    private static final String LOG_ARG = "log";
    private static final String SERVICES_ARG = "services";
    private static final String COMMAND_ARG = "command";

    enum _ {

        command_null("Install packages command argument '%s' must be set"),

        services_null("Services list argument '%s' must be set"),

        log_null("Logger argument '%s' must be set"),

        restarted_service_trace("Restarted service {} for {}, {}."),

        restarted_service_debug("Restarted service {} for {}."),

        restarted_service_info("Restarted service for script {}.");

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
     * Sets the context of the logger to {@link RestartServices}.
     */
    public RestartServicesLogger() {
        super(RestartServices.class);
    }

    void installPackagesDone(Object parent, ProcessTask task,
            Map<String, Object> args) {
        if (isTraceEnabled()) {
            trace(restarted_service_trace, args, parent, task);
        } else if (isDebugEnabled()) {
            debug(restarted_service_debug, args, parent);
        } else {
            info(restarted_service_info, parent);
        }
    }

    void checkArgs(Map<String, Object> args) {
        notNull(args.get(COMMAND_ARG), command_null.toString(), COMMAND_ARG);
        notNull(args.get(SERVICES_ARG), services_null.toString(), SERVICES_ARG);
        notNull(args.get(LOG_ARG), log_null.toString(), LOG_ARG);
    }
}
