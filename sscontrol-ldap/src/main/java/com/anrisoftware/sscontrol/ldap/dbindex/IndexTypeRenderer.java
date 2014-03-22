/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.dbindex;

import java.io.Serializable;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

/**
 * Renderer for database index type.
 * 
 * @see IndexType
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class IndexTypeRenderer implements AttributeRenderer, Serializable {

	@Override
	public String toString(Object o, String formatString, Locale locale) {
		IndexType type = (IndexType) o;
		switch (type) {
		case approx:
			return "approx";
		case equality:
			return "eq";
		case present:
			return "pres";
		case substring:
			return "sub";
		case none:
			return "none";
		}
		return o.toString();
	}

}
