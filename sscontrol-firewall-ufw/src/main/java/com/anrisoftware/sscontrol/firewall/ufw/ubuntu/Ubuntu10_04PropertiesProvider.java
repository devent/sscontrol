package com.anrisoftware.sscontrol.firewall.ufw.ubuntu;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default UFW Ubuntu 10.04 properties from
 * {@code ufw_ubuntu_10_04.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code install_command}</dt>
 * <dd>the default packages installation command;</dd>
 * 
 * <dt>{@code ufw_command}</dt>
 * <dd>the UFW command;</dd>
 * 
 * <dt>{@code packages}</dt>
 * <dd>the list of needed packages for the service.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
			.getResource("/ufw_ubuntu_10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
