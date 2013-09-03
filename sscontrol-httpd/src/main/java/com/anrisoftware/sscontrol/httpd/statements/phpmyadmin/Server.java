package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Server {

	@Inject
	private ServerLogger log;

	private String host;

	private int port;

	public void setHost(String host) {
		log.checkHost(host);
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setPort(Integer port) {
		log.checkPort(port);
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("host", host)
				.append("port", port).toString();
	}
}
