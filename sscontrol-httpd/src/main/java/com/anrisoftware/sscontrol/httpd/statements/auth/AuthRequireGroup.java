package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Group for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthRequireGroup extends AuthRequire {

	private static final String GROUP = "group";

	private final String name;

	/**
	 * @see AuthRequireFactory#group(Map)
	 */
	@Inject
	AuthRequireGroup(AuthRequireGroupLogger log,
			@Assisted Map<String, Object> map) {
		log.checkGroupName(map);
		this.name = (String) map.get(GROUP);
	}

	/**
	 * Returns the name of the group.
	 * 
	 * @return the group name.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
