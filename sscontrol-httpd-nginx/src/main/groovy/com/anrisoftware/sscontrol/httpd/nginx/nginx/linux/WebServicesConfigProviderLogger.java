package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.WebServicesConfigProviderLogger._.error_find_service;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.WebServicesConfigProviderLogger._.error_find_service_message;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.WebServicesConfigProviderLogger._.service_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo;

/**
 * Logging for {@link WebServicesConfigProvider}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WebServicesConfigProviderLogger extends AbstractLogger {

    enum _ {

        error_find_service("Error find service"),

        service_info("Service info"),

        error_find_service_message("Error find service {}.");

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
     * Sets the context of the logger to {@link WebServicesConfigProvider}.
     */
    public WebServicesConfigProviderLogger() {
        super(WebServicesConfigProvider.class);
    }

    ServiceException errorFindService(ServiceConfigInfo info) {
        return logException(new ServiceException(error_find_service).add(
                service_info, info), error_find_service_message, info);
    }
}
