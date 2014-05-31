package com.anrisoftware.sscontrol.httpd.gitit.systemv;

import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_defaults_file_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvServiceLogger._.service_file_created_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SystemvService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SystemvServiceLogger extends AbstractLogger {

    enum _ {

        service_defaults_file_created_trace(
                "Service defaults configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_defaults_file_created_debug(
                "Service defaults configuration '{}' created for {}."),

        service_defaults_file_created_info(
                "Service defaults configuration '{}' created."),

        service_file_created_trace(
                "Service configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_file_created_debug("Service configuration '{}' created for {}."),

        service_file_created_info("Service configuration '{}' created.");

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
     * Sets the context of the logger to {@link SystemvService}.
     */
    public SystemvServiceLogger() {
        super(SystemvService.class);
    }

    void serviceDefaultsFileCreated(Object config, File file, String configstr) {
        if (isTraceEnabled()) {
            trace(service_defaults_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_defaults_file_created_debug, file, config);
        } else {
            info(service_defaults_file_created_info, file);
        }
    }

    void serviceFileCreated(Object config, File file, String configstr) {
        if (isTraceEnabled()) {
            trace(service_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_file_created_debug, file, config);
        } else {
            info(service_file_created_info, file);
        }
    }
}
