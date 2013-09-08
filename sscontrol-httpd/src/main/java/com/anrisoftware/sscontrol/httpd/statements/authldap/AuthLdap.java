package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.statements.auth.SatisfyType;
import com.google.inject.assistedinject.Assisted;

public class AuthLdap extends AbstractAuth {

	private static final String SATISFY = "satisfy";

	private static final String LOCATION = "location";

	private static final String TYPE = "type";

	private static final String PROVIDER = "provider";

	private AuthLdapLogger log;

	private Map<String, Object> args;

	@Inject
	AuthLdap(@Assisted Map<String, Object> args, @Assisted String name) {
		super(name);
		this.args = args;
	}

	@Inject
	void setAuthLogger(AuthLdapLogger logger) {
		this.log = logger;
		setLocation(args.get(LOCATION));
		if (args.containsKey(TYPE)) {
			setType((AuthType) args.get(TYPE));
		}
		if (args.containsKey(PROVIDER)) {
			setProvider((AuthProvider) args.get(PROVIDER));
		}
		if (args.containsKey(SATISFY)) {
			setSatisfy((SatisfyType) args.get(SATISFY));
		}
		this.args = null;
	}

	public void setLocation(Object location) {
		log.checkLocation(location);
		super.setLocation(location.toString());
	}
}
