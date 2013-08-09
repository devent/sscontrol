/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

@SuppressWarnings("serial")
public class BindAddresses implements Serializable {

	/**
	 * Bind to all network interfaces.
	 */
	public static final BindAddresses ALL = new BindAddresses("all");

	/**
	 * Bind on the loopback network interface only.
	 */
	public static final BindAddresses LOOPBACK = new BindAddresses("loopback");

	private final List<String> addresses;

	/**
	 * @see BindAddressesFactory#create(String)
	 */
	@Inject
	BindAddresses(@Assisted String addresses) {
		this.addresses = asList(StringUtils.split(addresses, " ,;"));
	}

	/**
	 * Returns the addresses to bind.
	 * 
	 * @return an unmodifiable {@link List}.
	 */
	public List<String> getAddresses() {
		return unmodifiableList(addresses);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("addresses", addresses)
				.toString();
	}
}
