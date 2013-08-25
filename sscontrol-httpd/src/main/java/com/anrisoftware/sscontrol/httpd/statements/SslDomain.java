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
