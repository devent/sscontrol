/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-database.
 * 
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Binding for database server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Binding implements Serializable {

	private static final String ADDRESS = "address";

	@Inject
	private BindingLogger log;

	private Map<String, Object> args;

	private String address;

	/**
	 * @see BindingFactory#create(Map)
	 */
	@Inject
	Binding(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setBindLogger(BindingLogger logger) {
		this.log = logger;
		setAddress(args.get(ADDRESS));
		args = null;
	}

	private void setAddress(Object object) {
		log.checkAddress(object);
		this.address = object.toString();
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ADDRESS, address).toString();
	}

}
