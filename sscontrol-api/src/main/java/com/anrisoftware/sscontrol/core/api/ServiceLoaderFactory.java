package com.anrisoftware.sscontrol.core.api;

import java.util.Map;

/**
 * Factory to create the service loader.
 * 
 * @see ServiceLoader
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceLoaderFactory {

	/**
	 * Creates the service loader.
	 * 
	 * @param registry
	 *            the {@link ServicesRegistry} to which to add the service.
	 * 
	 * @param variables
	 *            a {@link Map} of variables that should be injected in the
	 *            script. The map should contain entries
	 *            {@code [<variable name>=<value>, ...]}.
	 * 
	 * @return the {@link ServiceLoader}.
	 */
	ServiceLoader create(ServicesRegistry registry,
			Map<String, Object> variables);
}
