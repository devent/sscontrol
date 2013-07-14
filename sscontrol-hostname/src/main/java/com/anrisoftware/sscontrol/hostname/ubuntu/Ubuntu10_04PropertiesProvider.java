package com.anrisoftware.sscontrol.hostname.ubuntu;

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
 * <dt>{@code install_command}</dt>
 * <dd>the default packages installation command;</dd>
 * 
 * <dt>{@code restart_command}</dt>
 * <dd>the default service restart command;</dd>
 * 
 * <dt>{@code configuration_directory}</dt>
 * <dd>the default directory for the configuration files.</dd>
 * 
 * <dt>{@code configuration_file}</dt>
 * <dd>the hostname configuration file name.</dd>
 * 
 * <dt>{@code packages}</dt>
 * <dd>the list of needed packages for the service.</dd>
 * 
 * <dt>{@code restart_services}</dt>
 * <dd>the list of services that should be restarted.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
			.getResource("/hostname_ubuntu10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
