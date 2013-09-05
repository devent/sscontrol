package com.anrisoftware.sscontrol.httpd.statements.domain;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainUser {

	@Inject
	private DomainUserLogger log;

	private String name;

	private String group;

	public void setUser(String name) {
		log.checkUser(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setGroup(Object group) {
		log.checkGroup(group);
		this.group = group.toString();
	}

	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("user", name)
				.append("group", group).toString();
	}
}
