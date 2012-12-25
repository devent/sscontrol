package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Allow from an address, port, protocol to an address, port, protocol on the
 * host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AllowFrom implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -5291719795852192982L;

	private final transient PortFactory portFactory;

	private final transient AddressFactory addressFactory;

	private final Address address;

	private final Port port;

	private final Protocol proto;

	private Address toAddress;

	private Port toPort;

	private Protocol toProto;

	/**
	 * Sets allow from the specified address, network port and network protocol.
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
	AllowFrom(PortFactory portFactory, AddressFactory addressFactory,
			@Assisted Address address, @Assisted Port port,
			@Assisted Protocol proto) {
		this.portFactory = portFactory;
		this.addressFactory = addressFactory;
		this.address = address;
		this.port = port;
		this.proto = proto;
	}

	public void to(String address) {
		to(address, (Integer) null, Protocol.TCPUDP);
	}

	public void to(Address address) {
		to(address, (Integer) null, Protocol.TCPUDP);
	}

	public void to(String address, Integer port) {
		to(address, port, Protocol.TCPUDP);
	}

	public void to(Address address, Integer port) {
		to(address, port, Protocol.TCPUDP);
	}

	public void to(String address, Integer port, Protocol proto) {
		Port networkPort = port == null ? portFactory.undefinedPort()
				: portFactory.fromPortNumber(port);
		to(address, networkPort, proto);
	}

	public void to(Address address, Integer port, Protocol proto) {
		Port networkPort = port == null ? portFactory.undefinedPort()
				: portFactory.fromPortNumber(port);
		to(address, networkPort, proto);
	}

	public void to(String address, String service) {
		to(address, service, Protocol.TCPUDP);
	}

	public void to(Address address, String service) {
		to(address, service, Protocol.TCPUDP);
	}

	public void to(String address, String service, Protocol proto) {
		to(address, portFactory.fromServiceName(service), proto);
	}

	public void to(Address address, String service, Protocol proto) {
		to(address, portFactory.fromServiceName(service), proto);
	}

	public void to(String address, Port port, Protocol proto) {
		to(addressFactory.fromAddress(address), port, proto);
	}

	public void to(Address address, Port port, Protocol proto) {
		this.toAddress = address;
		this.toPort = port;
		this.toProto = proto;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("address", address)
				.append("port", port).append("protocol", proto)
				.append("to address", toAddress).append("to port", toPort)
				.append("to proto", toProto).toString();
	}
}
