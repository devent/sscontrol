package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/group attribute.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthAttribute {

	private static final String DN = "dn";

	private final AuthAttributeLogger log;

	private String name;

	private AttributeDn dn;

	/**
	 * @see AuthAttributeFactory#create(Map, String)
	 */
	@Inject
	AuthAttribute(AuthAttributeLogger log, @Assisted Map<String, Object> args,
			@Assisted String name) {
		this.log = log;
		this.dn = AttributeDn.on;
		setName(name);
		if (args.containsKey(DN)) {
			setDn((Boolean) args.get(DN));
		}
	}

	public void setName(String name) {
		log.checkAttribute(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDn(boolean dn) {
		this.dn = dn ? AttributeDn.on : AttributeDn.off;
	}

	public AttributeDn getDn() {
		return dn;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append("is dn", dn)
				.toString();
	}
}
