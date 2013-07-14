package com.anrisoftware.sscontrol.dns.service;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default DNS service properties from
 * {@code dns_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code default_bind_addresses}</dt>
 * <dd>the list of the default binding addresses;</dd>
 * 
 * <dt>{@code default_ttl}</dt>
 * <dd>the default time to live time;</dd>
 * 
 * <dt>{@code default_refresh}</dt>
 * <dd>the default refresh time.</dd>
 * 
 * <dt>{@code default_retry}</dt>
 * <dd>the default retry time.</dd>
 * 
 * <dt>{@code default_expire}</dt>
 * <dd>the default expire time.</dd>
 * 
 * <dt>{@code default_minimum_ttl}</dt>
 * <dd>the default minimum time to live time.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DnsPropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DnsPropertiesProvider.class
			.getResource("/dns_defaults.properties");

	DnsPropertiesProvider() {
		super(DnsPropertiesProvider.class, RESOURCE);
	}

}
