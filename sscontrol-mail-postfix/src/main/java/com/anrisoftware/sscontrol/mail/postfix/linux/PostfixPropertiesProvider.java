package com.anrisoftware.sscontrol.mail.postfix.linux;

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
public class PostfixPropertiesProvider extends
		AbstractContextPropertiesProvider {

	private static final URL RESOURCE = PostfixPropertiesProvider.class
			.getResource("/postfix.properties");

	PostfixPropertiesProvider() {
		super(PostfixScript.class, RESOURCE);
	}

}
