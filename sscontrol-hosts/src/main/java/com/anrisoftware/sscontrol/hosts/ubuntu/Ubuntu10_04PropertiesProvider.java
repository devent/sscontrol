package com.anrisoftware.sscontrol.hosts.ubuntu;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default dhclient properties from
 * {@code dhclient_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dl>
 * <dt>{@code configuration_directory}</dt>
 * <dd>the default directory for the configuration files.</dd>
 * 
 * <dt>{@code configuration_file}</dt>
 * <dd>the hostname configuration file name.</dd>
 * 
 * <dt>{@code default_hosts}</dt>
 * <dd>the list of the default hosts.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
			.getResource("/hosts_ubuntu10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
