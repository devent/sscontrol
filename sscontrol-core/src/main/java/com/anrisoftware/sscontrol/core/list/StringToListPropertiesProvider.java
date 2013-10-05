package com.anrisoftware.sscontrol.core.list;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * @see StringToList
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class StringToListPropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCES = StringToListPropertiesProvider.class
			.getResource("/sscontrol_core.properties");

	protected StringToListPropertiesProvider() {
		super(StringToListPropertiesProvider.class, RESOURCES);
	}

}
