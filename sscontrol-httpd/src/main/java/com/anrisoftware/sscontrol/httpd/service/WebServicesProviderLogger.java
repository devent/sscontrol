package com.anrisoftware.sscontrol.httpd.service;

import static com.anrisoftware.sscontrol.httpd.service.WebServicesProviderLogger._.error_find_service;
import static com.anrisoftware.sscontrol.httpd.service.WebServicesProviderLogger._.error_find_service_message;
import static com.anrisoftware.sscontrol.httpd.service.WebServicesProviderLogger._.service_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;

/**
 * Logging for {@link WebServicesProvider}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class WebServicesProviderLogger extends AbstractLogger {

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
     * Sets the context of the logger to {@link WebServicesProvider}.
     */
    public WebServicesProviderLogger() {
        super(WebServicesProvider.class);
    }

    ServiceException errorFindService(WebServiceInfo info) {
        return logException(new ServiceException(error_find_service).add(
                service_info, info), error_find_service_message, info);
    }
}
