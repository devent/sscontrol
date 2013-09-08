package com.anrisoftware.sscontrol.ldap.statements;

import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * The administrator user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Admin {

	private final AdminLogger log;

	private final String name;

	private final DomainComponent domain;

	private final Scripts scripts;

	private String password;

	private String description;

	/**
	 * @see AdminFactory#create(Map, DomainComponent, String)
	 */
	@Inject
	Admin(AdminLogger log, Scripts scripts, @Assisted Map<String, Object> args,
			@Assisted DomainComponent domain, @Assisted String name) {
		this.log = log;
		this.name = name;
		this.domain = domain;
		this.scripts = scripts;
		setPassword(args.get("password"));
		setDescription(args.get("description"));
	}

	public String getName() {
		return name;
	}

	public void setPassword(Object password) {
		log.checkPassword(password);
		this.password = password.toString();
	}

	public String getPassword() {
		return password;
	}

	public void setDescription(Object description) {
		this.description = description.toString();
	}

	public String getDescription() {
		return description;
	}

	public DomainComponent getDomain() {
		return domain;
	}

	public void script(Object resource) throws URISyntaxException {
		this.scripts.addResource(resource);
	}

	public Scripts getScripts() {
		return scripts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append(domain)
				.toString();
	}
}
