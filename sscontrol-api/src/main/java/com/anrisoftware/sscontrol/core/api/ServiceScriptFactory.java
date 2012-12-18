package com.anrisoftware.sscontrol.core.api;

/**
 * Factory that creates the service script for the specified service
 * information.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceScriptFactory {

	/**
	 * Retruns the service script.
	 * 
	 * @return the service script.
	 * 
	 * @throws ServiceException
	 *             if there was an error returning the service script.
	 */
	Object getScript() throws ServiceException;

	/**
	 * Returns the information by which the service script is identified.
	 * 
	 * @return the {@link ServiceScriptInfo}.
	 */
	ServiceScriptInfo getInfo();

	/**
	 * Sets the parent for the service script.
	 * 
	 * @param parent
	 *            the parent.
	 */
	void setParent(Object parent);

}
