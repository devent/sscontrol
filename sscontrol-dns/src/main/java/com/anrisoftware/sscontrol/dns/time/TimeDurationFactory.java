package com.anrisoftware.sscontrol.dns.time;

import java.util.Map;

/**
 * Factory to create the time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface TimeDurationFactory {

	/**
	 * Creates the time duration.
	 * 
	 * @param parent
	 *            the parent {@link Object}.
	 * 
	 * @param args
	 *            the {@link Map} arguments.
	 * 
	 * @return the {@link TimeDuration}.
	 */
	TimeDuration create(Object parent, Map<String, Object> args);
}
