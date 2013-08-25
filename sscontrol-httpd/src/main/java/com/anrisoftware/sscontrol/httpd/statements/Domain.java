package com.anrisoftware.sscontrol.httpd.statements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Domain entry.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Domain {

	private static final String USE = "use";
	private static final String ROOT = "root";
	private static final String PORT = "port";
	private static final String ADDRESS = "address";

	@Inject
	private DomainLogger log;

	@Inject
	private RedirectFactory redirectFactory;

	private final String name;

	private final List<Redirect> redirects;

	private String address;

	private int port;

	private String documentRoot;

	private String useDomains;

	/**
	 * @see DomainFactory#create(Map, String)
	 */
	@Inject
	Domain(@Assisted Map<String, Object> args, @Assisted String name) {
		this.name = name;
		this.redirects = new ArrayList<Redirect>();
		if (args.containsKey(ADDRESS)) {
			this.address = (String) args.get(ADDRESS);
		}
		if (args.containsKey(PORT)) {
			this.port = (Integer) args.get(PORT);
		}
		if (args.containsKey(ROOT)) {
			this.documentRoot = (String) args.get(ROOT);
		}
		if (args.containsKey(USE)) {
			this.useDomains = (String) args.get(USE);
		}
	}

	public String getName() {
		return name;
	}

	public void address(String address) {
		this.address = address;
		log.addressSet(this, address);
	}

	public String getAddress() {
		return address;
	}

	public void port(int port) {
		this.port = port;
		log.portSet(this, port);
	}

	public int getPort() {
		return port;
	}

	public void documentRoot(String root) {
		this.documentRoot = root;
		log.documentRootSet(this, root);
	}

	public String getDocumentRoot() {
		return documentRoot;
	}

	public void useDomain(String use) {
		this.useDomains = use;
		log.useDomainSet(this, use);
	}

	public String getUseDomains() {
		return useDomains;
	}

	public void redirect(Object s) {
	}

	public void to_www() {
		Redirect redirect = redirectFactory.createToWww();
		redirects.add(redirect);
		log.redirectToWwwAdded(this, redirect);
	}

	public void http_to_https() {
		Redirect redirect = redirectFactory.createHttpToHttps();
		redirects.add(redirect);
		log.redirectHttpToHttpsAdded(this, redirect);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}