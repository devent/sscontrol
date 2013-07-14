package com.anrisoftware.sscontrol.mail.postfix.ubuntu;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the postfix properties.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
			.getResource("/postfix_ubuntu_10_04.properties");

	Ubuntu10_04PropertiesProvider() {
		super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
	}

}
