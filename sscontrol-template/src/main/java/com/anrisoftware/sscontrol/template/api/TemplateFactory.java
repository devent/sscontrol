package com.anrisoftware.sscontrol.template.api;

import java.net.URL;
import java.util.Properties;

/**
 * Factory to create a new template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface TemplateFactory {

	/**
	 * Creates a new template from the specified resource URL.
	 * 
	 * @param resource
	 *            the {@link URL} of the template file.
	 * 
	 * @param properties
	 *            the {@link Properties} for the template file.
	 * 
	 * @return the {@link TemplateWorker}. if there was an error creating the
	 *         template.
	 */
	Template create(URL resource, Properties properties)
			throws TemplateException;
}
