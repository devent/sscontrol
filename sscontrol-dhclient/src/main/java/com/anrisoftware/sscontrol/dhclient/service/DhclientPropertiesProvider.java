package com.anrisoftware.sscontrol.dhclient.service;

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
 * <dt>{@code default_option}</dt>
 * <dd>list of default options;</dd>
 * 
 * <dt>{@code default_sends}</dt>
 * <dd>list of default sends;</dd>
 * 
 * <dt>{@code default_requests}</dt>
 * <dd>list of default requests.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DhclientPropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DhclientPropertiesProvider.class
			.getResource("/dhclient_defaults.properties");

	DhclientPropertiesProvider() {
		super(DhclientPropertiesProvider.class, RESOURCE);
	}

}
