package com.anrisoftware.sscontrol.httpd.service;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;

/**
 * Loads the web services.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class WebServicesProvider implements
        Provider<ServiceLoader<WebServiceFactoryFactory>> {

    private final ServiceLoader<WebServiceFactoryFactory> services;

    @Inject
    private WebServicesProviderLogger log;

    public WebServicesProvider() {
        this.services = ServiceLoader.load(WebServiceFactoryFactory.class);
    }

    @Override
    public ServiceLoader<WebServiceFactoryFactory> get() {
        return services;
    }

    /**
     * Find the web service for the specified info.
     * 
     * @param info
     *            the {@link WebServiceInfo}.
     * 
     * @return the {@link WebServiceFactoryFactory}
     * 
     * @throws ServiceException
     *             if the service could not be found.
     */
    public WebServiceFactoryFactory find(WebServiceInfo info)
            throws ServiceException {
        for (WebServiceFactoryFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
