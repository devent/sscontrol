package com.anrisoftware.sscontrol.httpd.webserviceargs;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Factory to create the default web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DefaultWebServiceFactory {

    /**
     * Creates the default web service.
     * 
     * @param serviceName
     *            the service {@link String} name.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @param domain
     *            the {@link Domain} of the service.
     * 
     * @return the {@link DefaultWebService}.
     */
    DefaultWebService create(String serviceName, Map<String, Object> args,
            Domain domain);
}
