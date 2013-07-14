package com.anrisoftware.sscontrol.dns.maradns.ubuntu;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default MaraDNS Ubuntu 10.04 properties from
 * {@code maradns_ubuntu_10_04.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code install_command}</dt>
 * <dd>the default packages installation command;</dd>
 * 
 * <dt>{@code restart_command}</dt>
 * <dd>the default service restart command;</dd>
 * 
 * <dt>{@code enable_repository_command}</dt>
 * <dd>the default enable repositories command.</dd>
 * 
 * <dt>{@code configuration_directory}</dt>
 * <dd>the default directory for the configuration files.</dd>
 * 
 * <dt>{@code configuration_file}</dt>
 * <dd>the Mararc configuration file name.</dd>
 * 
 * <dt>{@code distribution_name}</dt>
 * <dd>the distribution name to enable addition repositories.</dd>
 * 
 * <dt>{@code additional_repositories}</dt>
 * <dd>the list of additional repositories to enable.</dd>
 * 
 * <dt>{@code repository_string}</dt>
 * <dd>the repository string to enable addition repositories.</dd>
 * 
 * <dt>{@code repository_string}</dt>
 * <dd>the list of needed system packages.</dd>
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
			.getResource("/maradns_ubuntu_10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
