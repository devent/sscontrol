package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractRequireGroup;
import com.google.inject.assistedinject.Assisted;

/**
 * Required LDAP/group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireLdapValidGroup extends AbstractRequireGroup {

	private static final String GROUP = "group";

	private RequireLdapValidGroupLogger log;

	@Inject
	private AuthAttributeFactory attributeFactory;

	private AuthAttribute attribute;

	private Map<String, Object> args;

	/**
	 * @see RequireLdapValidGroupFactory#create(Map)
	 */
	@Inject
	RequireLdapValidGroup(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setAuthRequireGroupLogger(RequireLdapValidGroupLogger logger) {
		this.log = logger;
		setName(args.get(GROUP));
		args = null;
	}

	private void setName(Object name) {
		log.checkName(name);
		setName(name.toString());
	}

	public void attribute(String name) {
		attribute(new HashMap<String, Object>(), name);
	}

	public void attribute(Map<String, Object> args, String name) {
		this.attribute = attributeFactory.create(args, name);
		log.attributeSet(this, attribute);
	}

	public AuthAttribute getAttribute() {
		return attribute;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("attribute", attribute).toString();
	}
}