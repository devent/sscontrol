package com.anrisoftware.sscontrol.mail.postfix.linux;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the postfix properties with MySQL database.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
@SuppressWarnings("serial")
public class MysqlPropertiesProvider extends AbstractContextPropertiesProvider {

	private static final URL RESOURCE = MysqlPropertiesProvider.class
			.getResource("/mysql_postfix.properties");

	@Inject
	MysqlPropertiesProvider(PostfixPropertiesProvider postfixProperties) {
		super(PostfixScript.class, RESOURCE);
		setDefaultProperties(postfixProperties.get());
	}

}
