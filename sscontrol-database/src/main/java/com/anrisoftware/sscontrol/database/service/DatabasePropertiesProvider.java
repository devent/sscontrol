package com.anrisoftware.sscontrol.database.service;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default database properties from
 * {@code database_defaults.properties}.
 * <p>
 * <h2>Properties</h2>
 * <p>
 * 
 * <dl>
 * <dt>{@code debugging}</dt>
 * <dd>set to {@code true} to enable debugging logging;</dd>
 * 
 * <dt>{@code bind_address}</dt>
 * <dd>IP address or host name to bind the server;</dd>
 * 
 * <dt>{@code user_server}</dt>
 * <dd>default server for new users.</dd>
 * </dl>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DatabasePropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = DatabaseModule.class
			.getResource("/database_defaults.properties");

	DatabasePropertiesProvider() {
		super(DatabasePropertiesProvider.class, RESOURCE);
	}

}
