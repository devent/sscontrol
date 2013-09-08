package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/authentication host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthHost {

	private static final String URL = "url";

	private AuthHostLogger log;

	private Map<String, Object> args;

	private final String name;

	private String url;

	@Inject
	AuthHost(@Assisted Map<String, Object> args, @Assisted String name) {
		this.name = name;
		this.args = args;
	}

	@Inject
	void setAuthHostLogger(AuthHostLogger logger) {
		this.log = logger;
		if (args.containsKey(URL)) {
			setUrl(args.get(URL));
		}
		args = null;
	}

	public String getName() {
		return name;
	}

	public void setUrl(Object url) {
		log.checkUrl(url);
		this.url = url.toString();
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append("url", url)
				.toString();
	}
}
