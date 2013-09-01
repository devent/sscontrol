package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Auth {

	private static final String GROUP = "group";

	private static final String NAME = "name";

	private static final String PROVIDER = "provider";

	@Inject
	private AuthLogger log;

	@Inject
	private AuthRequireFactory requireFactory;

	@Inject
	private AuthUserFactory userFactory;

	private final List<String> locations;

	private final List<AuthRequire> requires;

	private final List<AuthUser> users;

	private AuthProvider provider;

	private AuthType type;

	private String name;

	/**
	 * @see AuthFactory#create(Map)
	 */
	@Inject
	Auth(@Assisted Map<String, Object> map) {
		this.locations = new ArrayList<String>();
		this.requires = new ArrayList<AuthRequire>();
		this.users = new ArrayList<AuthUser>();
		if (map.containsKey(PROVIDER)) {
			this.provider = (AuthProvider) map.get(PROVIDER);
		}
		if (map.containsKey("type")) {
			this.type = (AuthType) map.get("type");
		}
		if (map.containsKey(NAME)) {
			this.name = (String) map.get(NAME);
		}
	}

	public String getName() {
		return name;
	}

	public AuthType getType() {
		return type;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void location(String location) {
		log.checkLocation(this, location);
		locations.add(location);
		log.locationAdd(this, location);
	}

	public List<String> getLocations() {
		if (locations.size() == 0) {
			locations.add("");
		}
		return locations;
	}

	public void valid_user() {
		AuthRequireValidUser require = requireFactory.validUser();
		requires.add(require);
		log.requireValidUserAdded(this, require);
	}

	public void require(Object s) {
	}

	public void require(Map<String, Object> map) {
		if (map.containsKey(GROUP)) {
			AuthRequireGroup require = requireFactory.group(map);
			requires.add(require);
			log.requireGroupAdded(this, require);
		}
	}

	public List<AuthRequire> getRequires() {
		return requires;
	}

	public void user(Map<String, Object> map, String name) {
		AuthUser user = userFactory.create(map, name);
		users.add(user);
		log.userAdded(this, user);
	}

	public List<AuthUser> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
