package com.anrisoftware.sscontrol.firewall.statements;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Deny from an address, port, protocol to an address, port, protocol on the
 * host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DenyFrom extends AllowFrom {

	/**
	 * @see DenyFromFactory#create(Address, Port, Protocol)
	 */
	@Inject
	DenyFrom(PortFactory portFactory, AddressFactory addressFactory,
			@Assisted Address address, @Assisted Port port,
			@Assisted Protocol proto) {
		super(portFactory, addressFactory, address, port, proto);
	}

}
