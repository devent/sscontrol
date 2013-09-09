package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.statements.auth.SatisfyType;
import com.anrisoftware.sscontrol.httpd.statements.yesno.YesNoFlag;
import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthLdap extends AbstractAuth {

	private static final String SATISFY = "satisfy";

	private static final String LOCATION = "location";

	private static final String TYPE = "type";

	private static final String PROVIDER = "provider";

	private static final String GROUP = "group";

	private AuthLdapLogger log;

	private Map<String, Object> args;

	@Inject
	private AuthHostFactory hostFactory;

	@Inject
	private CredentialsFactory credentialsFactory;

	@Inject
	private RequireLdapValidGroupFactory validGroupFactory;

	private AuthHost host;

	private Credentials credentials;

	private Authoritative authoritative;

	@Inject
	AuthLdap(@Assisted Map<String, Object> args, @Assisted String name) {
		super(name);
		this.authoritative = Authoritative.on;
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
		if (args.containsKey("authoritative")) {
			setAuthoritative((YesNoFlag) args.get("authoritative"));
		}
		this.args = null;
	}

	public void setLocation(Object location) {
		log.checkLocation(location);
		super.setLocation(location.toString());
	}

	public void setAuthoritative(YesNoFlag flag) {
		switch (flag) {
		case yes:
			setAuthoritative(Authoritative.on);
			break;
		case no:
			setAuthoritative(Authoritative.off);
			break;
		}
	}

	public void setAuthoritative(Authoritative authoritative) {
		this.authoritative = authoritative;
		log.authoritativeSet(this, authoritative);
	}

	public Authoritative getAuthoritative() {
		return authoritative;
	}

	public void host(Map<String, Object> args, String name) {
		this.host = hostFactory.create(args, name);
		log.hostSet(this, host);
	}

	public AuthHost getHost() {
		return host;
	}

	public void credentials(Map<String, Object> args, String name) {
		this.credentials = credentialsFactory.create(args, name);
		log.credentialsSet(this, credentials);
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public RequireLdapValidGroup require(Map<String, Object> args, Object s) {
		if (args.containsKey(GROUP)) {
			RequireLdapValidGroup require = validGroupFactory.create(args);
			addRequire(require);
			log.requireGroupAdded(this, require);
			return require;
		}
		return null;
	}
}
