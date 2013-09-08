package com.anrisoftware.sscontrol.ldap.organization;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.ldap.statements.Admin;
import com.anrisoftware.sscontrol.ldap.statements.AdminFactory;
import com.anrisoftware.sscontrol.ldap.statements.DomainComponent;
import com.google.inject.assistedinject.Assisted;

/**
 * The root organization.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Organization {

	private final OrganizationLogger log;

	private final String name;

	private final DomainComponent domain;

	@Inject
	private AdminFactory adminFactory;

	private Admin admin;

	/**
	 * @see OrganizationFactory#create(Map, String)
	 */
	@Inject
	Organization(OrganizationLogger log, DomainComponent domain,
			@Assisted Map<String, Object> args, @Assisted String name) {
		this.log = log;
		log.checkName(name);
		this.name = name;
		this.domain = domain;
		setDomain(args.get("domain"));
	}

	public String getName() {
		return name;
	}

	public void setDomain(Object domain) {
		log.checkDomain(domain);
		this.domain.setDomain(domain.toString());
	}

	public void admin(Map<String, Object> args, String name) {
		this.admin = adminFactory.create(args, domain, name);
		log.adminSet(this, admin);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append(domain)
				.toString();
	}
}
