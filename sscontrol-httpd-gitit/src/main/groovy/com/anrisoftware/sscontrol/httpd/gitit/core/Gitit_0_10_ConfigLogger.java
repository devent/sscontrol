package com.anrisoftware.sscontrol.httpd.gitit.core;

import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_trace;

import java.io.File;
import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging for {@link Gitit_0_10_Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Gitit_0_10_ConfigLogger extends AbstractLogger {

    enum _ {

        install_packages_done_trace("Install packages done {} in {}, {}."),

        install_packages_done_debug("Install packages done {} in {}."),

        install_packages_done_info("Install packages done {}."),

        default_config_created_trace(
                "Default configuration '{}' created for {}: \n>>>\n{}<<<"),

        default_config_created_debug(
                "Default configuration '{}' created for {}."),

        default_config_created_info(
                "Default configuration '{}' created for service '{}'."),

        service_defaults_file_created_trace(
                "Service defaults configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_defaults_file_created_debug(
                "Service defaults configuration '{}' created for {}."),

        service_defaults_file_created_info(
                "Service defaults configuration '{}' created for service '{}'."),

        service_file_created_trace(
                "Service configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_file_created_debug("Service configuration '{}' created for {}."),

        service_file_created_info(
                "Service configuration '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link Gitit_0_10_Config}.
     */
    public Gitit_0_10_ConfigLogger() {
        super(Gitit_0_10_Config.class);
    }

    void installCabalPackagesDone(Gitit_0_10_Config config,
            ScriptCommandWorker worker, @SuppressWarnings("rawtypes") Map args) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, args, config, worker);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, args, config);
        } else {
            info(install_packages_done_info, args);
        }
    }

    void defaultConfigCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(default_config_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(default_config_created_debug, file, config);
        } else {
            info(default_config_created_info, file, config.getServiceName());
        }
    }

    void serviceDefaultsFileCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(service_defaults_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_defaults_file_created_debug, file, config);
        } else {
            info(service_defaults_file_created_info, file,
                    config.getServiceName());
        }
    }

    void serviceFileCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(service_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_file_created_debug, file, config);
        } else {
            info(service_file_created_info, file, config.getServiceName());
        }
    }
}
