/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.statements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

/**
 * Scripts to import.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Scripts implements Iterable<String> {

	@Inject
	private ScriptsLogger log;

	private final List<Object> resources;

	public Charset charset;

	Scripts() {
		this.charset = Charset.defaultCharset();
		this.resources = new ArrayList<Object>();
	}

	/**
	 * Sets the character set to read resource files.
	 * 
	 * @param charset
	 *            the {@link Charset}.
	 */
	public void setCharset(Charset charset) {
		log.checkCharset(charset);
		this.charset = charset;
	}

	/**
	 * Adds the resource as script.
	 * 
	 * @param resource
	 *            the resource {@link URI}, {@link URL}, {@link File} or the
	 *            script text.
	 * 
	 * @throws URISyntaxException
	 *             if the specified URL is not formatted strictly according to
	 *             to RFC2396 and cannot be converted to a URI.
	 */
	public void addResource(Object resource) throws URISyntaxException {
		log.checkResource(resource);
		if (resource instanceof URI) {
			resources.add(resource);
		} else if (resource instanceof URL) {
			resources.add(((URL) resource).toURI());
		} else if (resource instanceof File) {
			resources.add(((File) resource).toURI());
		} else if (resource instanceof String) {
			resources.add(resource);
		} else {
			resources.add(resource.toString());
		}
	}

	@Override
	public Iterator<String> iterator() {
		return new Itr();
	}

	/**
	 * Reads and returns the script resources,
	 */
	private class Itr implements Iterator<String> {

		int index = 0;

		@Override
		public boolean hasNext() {
			return index != resources.size();
		}

		@Override
		public String next() {
			int i = index;
			if (i >= resources.size()) {
				throw new NoSuchElementException();
			}
			Object element = resources.get(i);
			if (element instanceof URI) {
				element = readResource(element);
			}
			index = i + 1;
			return element.toString();
		}

		private Object readResource(Object element) {
			URI resource = (URI) element;
			try {
				element = IOUtils.toString(resource, charset);
			} catch (IOException e) {
				throw log.readTextError(e, resource);
			}
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
