package com.anrisoftware.sscontrol.httpd.statements.auth;

import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AuthRequireGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthRequireGroupLogger extends AbstractLogger {

	enum _ {

		groupNameNull("Group name must be set.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link AuthRequireGroup}.
	 */
	public AuthRequireGroupLogger() {
		super(AuthRequireGroup.class);
	}

	void checkGroupName(Map<String, Object> map) {
		isTrue(map.containsKey("group"), _.groupNameNull.toString());
	}

}
