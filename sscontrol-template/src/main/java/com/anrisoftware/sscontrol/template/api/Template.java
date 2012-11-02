package com.anrisoftware.sscontrol.template.api;

import java.io.Serializable;

/**
 * Process a template with attributes.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Template extends Serializable {

	/**
	 * Process the template with the specified name and the attributes.
	 * 
	 * @param name
	 *            the name of the template.
	 * 
	 * @param data
	 *            the attributes for the template. Expecting first the attribute
	 *            name and then the attribute value.
	 * 
	 * @return the processed template string.
	 * 
	 * @throws TemplateException
	 *             if there was an error processing a template.
	 */
	String process(String name, Object... data) throws TemplateException;

}
