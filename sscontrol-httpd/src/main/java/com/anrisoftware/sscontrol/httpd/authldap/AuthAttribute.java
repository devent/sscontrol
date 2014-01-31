/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.authldap;

import static com.anrisoftware.sscontrol.core.yesno.YesNoFlag.yes;
import static com.anrisoftware.sscontrol.httpd.authldap.AttributeDn.off;
import static com.anrisoftware.sscontrol.httpd.authldap.AttributeDn.on;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
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
			setDn(args.get(DN));
		}
	}

	public void setName(String name) {
		log.checkAttribute(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setDn(Object object) {
		if (object instanceof Boolean) {
			setDn((Boolean) object);
		} else if (object instanceof YesNoFlag) {
			setDn((YesNoFlag) object);
		}
	}

	public void setDn(YesNoFlag flag) {
		this.dn = flag == yes ? on : off;
	}

	public void setDn(Boolean dn) {
		this.dn = dn ? on : off;
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
