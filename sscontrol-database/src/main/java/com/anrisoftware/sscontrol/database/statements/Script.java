/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-database.
 * 
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.statements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.assistedinject.Assisted;

/**
 * Database administrator.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Script implements Serializable {

	private static final String RESOURCE = "execute";

	@Inject
	private ScriptLogger log;

	private URI resource;

	private Map<String, Object> args;

	/**
	 * @see AdminFactory#create(Map)
	 */
	@Inject
	Script(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setAdminLogger(ScriptLogger logger) throws ServiceException {
		this.log = logger;
		setResource(args.get(RESOURCE));
		args = null;
	}

	private void setResource(Object object) throws ServiceException {
		log.checkResource(object);
		if (object instanceof URI) {
			this.resource = (URI) object;
		} else if (object instanceof URL) {
			this.resource = toURI((URL) object);
		} else if (object instanceof File) {
			this.resource = toURI((File) object);
		} else {
			this.resource = toURI(object.toString());
		}
	}

	private URI toURI(URL url) throws ServiceException {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw log.invalidUri(e, url);
		}
	}

	private URI toURI(File file) throws ServiceException {
		try {
			return file.toURI().toURL().toURI();
		} catch (URISyntaxException e) {
			throw log.invalidUri(e, file);
		} catch (MalformedURLException e) {
			throw log.invalidUrl(e, file);
		}
	}

	private URI toURI(String path) throws ServiceException {
		try {
			URI uri = new URI(path);
			if (uri.getScheme() == null) {
				return toURI(new File(path));
			} else {
				return uri.toURL().toURI();
			}
		} catch (URISyntaxException e) {
			throw log.invalidUri(e, path);
		} catch (MalformedURLException e) {
			throw log.invalidUrl(e, path);
		} catch (IllegalArgumentException e) {
			throw log.pathNotAbsolute(e, path);
		}
	}

	public URI getResource() {
		return resource;
	}

	/**
	 * Opens the resource for reading.
	 * 
	 * @return the {@link InputStream}.
	 * 
	 * @see URL#openStream()
	 */
	public InputStream openStream() throws MalformedURLException, IOException {
		return resource.toURL().openStream();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(RESOURCE, resource).toString();
	}

}
