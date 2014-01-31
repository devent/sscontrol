package com.anrisoftware.sscontrol.httpd.webservice;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Service configuration factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceConfigFactory {

    /**
     * Returns the service configuration.
     * 
     * @return the service {@link ServiceConfig} configuration.
     * 
     * @throws ServiceException
     *             if there was an error returning the service configuration.
     */
    ServiceConfig getScript() throws ServiceException;

    /**
     * Returns the information by which the service configuration is identified.
     * 
     * @return the {@link ServiceScriptInfo}.
     */
    ServiceConfigInfo getInfo();

    /**
     * Sets the parent for the service configuration.
     * 
     * @param parent
     *            the parent.
     */
    void setParent(Object parent);

}
