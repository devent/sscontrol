package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;
import java.util.Map;

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
		setTo(address, portFactory.undefinedPort(), Protocol.TCPUDP);
	}

	public void to(Address address) {
		setTo(address, portFactory.undefinedPort(), Protocol.TCPUDP);
	}

	public void to(Map<String, Object> args, String address) {
		to(args, addressFactory.fromAddress(address));
	}

	public void to(Map<String, Object> args, Address address) {
		Protocol proto = Protocol.TCPUDP;
		Port port = portFactory.undefinedPort();
		if (args.containsKey("proto")) {
			proto = (Protocol) args.get("proto");
		}
		if (args.containsKey("port")) {
			Object value = args.get("port");
			if (value instanceof Integer) {
				port = portFactory.fromPortNumber((Integer) value);
			} else {
				port = portFactory.fromServiceName(value.toString());
			}
		}
		setTo(address, port, proto);
	}

	private void setTo(String address, Port port, Protocol proto) {
		setTo(addressFactory.fromAddress(address), port, proto);
	}

	private void setTo(Address address, Port port, Protocol proto) {
		this.toAddress = address;
		this.toPort = port;
		this.toProto = proto;
	}

	public Address getAddress() {
		return address;
	}

	public Port getPort() {
		return port;
	}

	public Protocol getProto() {
		return proto;
	}

	public Address getToAddress() {
		return toAddress;
	}

	public Port getToPort() {
		return toPort;
	}

	public Protocol getToProto() {
		return toProto;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("address", address)
				.append("port", port).append("protocol", proto)
				.append("to address", toAddress).append("to port", toPort)
				.append("to proto", toProto).toString();
	}
}
