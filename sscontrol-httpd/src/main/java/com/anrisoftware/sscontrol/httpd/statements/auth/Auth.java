package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
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

	private final List<AuthRequire> requires;

	private final List<AuthUser> users;

	private final Domain domain;

	private AuthProvider provider;

	private String name;

	/**
	 * @see AuthFactory#create(Domain, Map)
	 */
	@Inject
	Auth(@Assisted Domain domain, @Assisted Map<String, Object> map) {
		this.domain = domain;
		this.requires = new ArrayList<AuthRequire>();
		this.users = new ArrayList<AuthUser>();
		if (map.containsKey(PROVIDER)) {
			this.provider = (AuthProvider) map.get(PROVIDER);
		}
		if (map.containsKey(NAME)) {
			this.name = (String) map.get(NAME);
		}
	}

	public Domain getDomain() {
		return domain;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public String getName() {
		return name;
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

	public void user(Map<String, Object> map, String name) {
		AuthUser user = userFactory.create(map, name);
		users.add(user);
		log.userAdded(this, user);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("domain", domain).toString();
	}
}
