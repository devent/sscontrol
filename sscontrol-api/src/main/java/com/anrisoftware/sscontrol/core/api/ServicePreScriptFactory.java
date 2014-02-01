package com.anrisoftware.sscontrol.core.api;

/**
 * Factory that creates the pre service script for the specified service
 * information.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServicePreScriptFactory {

    /**
     * Returns the service pre-script.
     * 
     * @return the service {@link ServicePreScript}.
     * 
     * @throws ServiceException
     *             if there was an error returning the service script.
     */
    ServicePreScript getPreScript() throws ServiceException;

    /**
     * Returns the information by which the service pre-script is identified.
     * 
     * @return the {@link ServiceScriptInfo}.
     */
    ServiceScriptInfo getInfo();

    /**
     * Sets the parent for the service pre-script.
     * 
     * @param parent
     *            the parent.
     */
    void setParent(Object parent);

}
