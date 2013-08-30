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
package com.anrisoftware.sscontrol.httpd.statements;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * SSL/TLS domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SslDomain extends Domain {

	private URL certificationFile;
	private URL certificationKeyFile;

	@Inject
	SslDomain(@Assisted Map<String, Object> args, @Assisted String name) {
		super(args, name);
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

	public void certification_file(URL url) {
		this.certificationFile = url;
	}

	/**
	 * Returns the certificate file URL.
	 * 
	 * @return the the certificate file {@link URL}.
	 */
	public URL getCertificationFile() {
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

	public void certification_key_file(URL url) {
		this.certificationKeyFile = url;
	}

	/**
	 * Returns the certificate key file URL.
	 * 
	 * @return the the certificate key file {@link URL}.
	 */
	public URL getCertificationKeyFile() {
		return certificationKeyFile;
	}
}
