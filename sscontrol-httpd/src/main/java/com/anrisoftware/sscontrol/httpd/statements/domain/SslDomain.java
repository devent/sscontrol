/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.domain;

import static java.lang.String.format;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect;
import com.google.inject.assistedinject.Assisted;

/**
 * SSL/TLS domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SslDomain extends Domain {

	@Inject
	private DomainLogger log;

	private URL certificationResource;

	private URL certificationKeyResource;

	private String certificationFile;

	private String certificationKeyFile;

	@Inject
	SslDomain(@Assisted Map<String, Object> args, @Assisted String name) {
		super(args, 443, name);
	}

	@Override
	public String getFileName() {
		return format("100-robobee-%s-ssl.conf", getName());
	}

	@Override
	public void to_www() {
		Redirect redirect = getRedirectFactory().createToWwwHttps(this);
		addRedirect(redirect);
		log.redirectToWwwAdded(this, redirect);
	}

	public void certification_file(Object path) throws MalformedURLException,
			URISyntaxException {
		certification_file(path.toString());
	}

	public void certification_file(String path) throws URISyntaxException,
			MalformedURLException {
		URI uri = new URI(path);
		if (!uri.isAbsolute()) {
			certification_file(new File(path).toURI().toURL());
		} else {
			certification_file(uri.toURL());
		}
	}

	public void certification_file(URL url) throws URISyntaxException {
		this.certificationResource = url;
		this.certificationFile = new File(url.getPath()).getName();
	}

	/**
	 * Returns the certificate resource.
	 * 
	 * @return the the certificate {@link URL} resource.
	 */
	public URL getCertificationResource() {
		log.checkCertificationResource(this, certificationResource);
		return certificationResource;
	}

	/**
	 * Returns the certificate file name.
	 * 
	 * @return the the certificate file name.
	 */
	public String getCertificationFile() {
		log.checkCertificationResource(this, certificationFile);
		return certificationFile;
	}

	public void certification_key_file(Object path)
			throws MalformedURLException, URISyntaxException {
		certification_file(path.toString());
	}

	public void certification_key_file(String path) throws URISyntaxException,
			MalformedURLException {
		URI uri = new URI(path);
		if (!uri.isAbsolute()) {
			certification_key_file(new File(path).toURI().toURL());
		} else {
			certification_key_file(uri.toURL());
		}
	}

	public void certification_key_file(URL url) throws URISyntaxException {
		this.certificationKeyResource = url;
		this.certificationKeyFile = new File(url.getPath()).getName();
	}

	/**
	 * Returns the certificate key resource.
	 * 
	 * @return the the certificate key {@link URL} resource.
	 */
	public URL getCertificationKeyResource() {
		log.checkCertificationResource(this, certificationKeyResource);
		return certificationKeyResource;
	}

	/**
	 * Returns the certificate key file path.
	 * 
	 * @return the the certificate key path.
	 */
	public String getCertificationKeyFile() {
		return certificationKeyFile;
	}

}
