package com.anrisoftware.sscontrol.httpd.roundcube.core;

import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.error_sample_config_file;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.error_sample_config_file_message;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.the_file;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link Roundcube_1_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Roundcube_1_ConfigLogger extends AbstractLogger {

    enum _ {

        message("message"),

        error_sample_config_file("Roundcube sample config file does not exist"),

        error_sample_config_file_message(
                "Roundcube sample config file '{}' does not exist for service '{}'."),

        the_file("file");

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
     * Sets the context of the logger to {@link Roundcube_1_Config}.
     */
    public Roundcube_1_ConfigLogger() {
        super(Roundcube_1_Config.class);
    }

    void checkRoundcubeSampleConfigFile(Roundcube_1_Config config, File file)
            throws ServiceException {
        if (!file.exists()) {
            throw logException(
                    new ServiceException(error_sample_config_file).add(
                            the_file, file), error_sample_config_file_message,
                    file, config.getServiceName());
        }
    }
}
