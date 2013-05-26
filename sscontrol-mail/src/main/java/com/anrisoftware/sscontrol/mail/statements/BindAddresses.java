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
