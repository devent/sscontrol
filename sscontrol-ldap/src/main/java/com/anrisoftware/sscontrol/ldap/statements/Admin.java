package com.anrisoftware.sscontrol.ldap.statements;

import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

public class Admin {

	private final AdminLogger log;

	private final String name;

	private final DomainComponent domain;

	private final Scripts scripts;

	private String password;

	@Inject
	Admin(AdminLogger log, DomainComponent domain, Scripts scripts,
			@Assisted Map<String, Object> args, @Assisted String name) {
		this.log = log;
		this.name = name;
		this.domain = domain;
		this.scripts = scripts;
		setPassword(args.get("password"));
		setDomain(args.get("domain"));
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

	public void setDomain(Object domain) {
		log.checkDomain(domain);
		this.domain.setDomain(domain.toString());
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
