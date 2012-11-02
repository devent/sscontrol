/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-template.
 *
 * sscontrol-template is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-template is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-template. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.template.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Information about the template service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class TemplateServiceInfo {

	private final String name;

	/**
	 * Sets the service name.
	 * 
	 * @param name
	 *            the service name.
	 */
	public TemplateServiceInfo(String name) {
		this.name = name;
	}

	/**
	 * Returns the service name.
	 * 
	 * @return the service name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Compare this service information to a string or to a different service
	 * information. If comparing to a string the service name is compared.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof String) {
			String thatName = (String) obj;
			return name.equals(thatName);
		}
		TemplateServiceInfo rhs = (TemplateServiceInfo) obj;
		return new EqualsBuilder().append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
