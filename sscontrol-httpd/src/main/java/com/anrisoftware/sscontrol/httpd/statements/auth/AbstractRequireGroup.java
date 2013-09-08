package com.anrisoftware.sscontrol.httpd.statements.auth;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Required authentication group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AbstractRequireGroup implements AuthRequire {

	private AbstractRequireGroupLogger log;

	private String name;

	@Inject
	void setAbstractRequireGroupLogger(AbstractRequireGroupLogger logger) {
		this.log = logger;
	}

	public void setName(String name) {
		log.checkName(name);
		this.name = name.toString();
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
