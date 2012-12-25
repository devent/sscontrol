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
public class DenyFrom extends AllowFrom {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -2311097866774038417L;

	/**
	 * Sets deny from the specified address, network port and network protocol.
	 * 
	 * @param portFactory
	 *            the {@link PortFactory} to create the destination network
	 *            port.
	 * 
	 * @param addressFactory
	 *            the {@link PortFactory} to create the destination network
	 *            address.
	 * 
	 * @param address
	 *            the source network {@link Address} address.
	 * 
	 * @param port
	 *            the source network {@link Port} port.
	 * 
	 * @param proto
	 *            the source network {@link Protocol} protocol.
	 */
	@Inject
	DenyFrom(PortFactory portFactory, AddressFactory addressFactory,
			@Assisted Address address, @Assisted Port port,
			@Assisted Protocol proto) {
		super(portFactory, addressFactory, address, port, proto);
	}

}
