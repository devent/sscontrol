/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.match.worker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.text.utils.SerializableCharset;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Compare the content of a text file to a given pattern.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class MatchTextWorker implements Worker {

	/**
	 * @since 0.1
	 */
	private static final long serialVersionUID = 3064256627303024488L;

	private final MatchTextWorkerLogger log;

	private final URL resource;

	private final Pattern pattern;

	private final SerializableCharset charset;

	private boolean matches;

	/**
	 * Sets the file, the comparison text and the character set.
	 * 
	 * @param logger
	 *            the {@link MatchTextWorkerLogger} for logging messages.
	 * 
	 * @param file
	 *            the {@link File} which content is read.
	 * 
	 * @param pattern
	 *            the comparison {@link Pattern}.
	 * 
	 * @param charset
	 *            the {@link Charset} of the text file.
	 * 
	 * @throws MalformedURLException
	 *             if the specified file can not be converted to a URL.
	 */
	@AssistedInject
	MatchTextWorker(MatchTextWorkerLogger logger, @Assisted File file,
			@Assisted Pattern pattern, @Assisted Charset charset)
			throws MalformedURLException {
		this.log = logger;
		this.resource = file.toURI().toURL();
		this.pattern = pattern;
		this.charset = new SerializableCharset(charset);
	}

	/**
	 * Sets the file, the comparison text and the character set.
	 * 
	 * @param logger
	 *            the {@link MatchTextWorkerLogger} for logging messages.
	 * 
	 * @param resource
	 *            the {@link URI} of the text resource which content is read.
	 * 
	 * @param pattern
	 *            the comparison {@link Pattern}.
	 * 
	 * @param charset
	 *            the {@link Charset} of the text file.
	 * 
	 * @throws MalformedURLException
	 *             if the specified resource URI can not be converted to a URL.
	 */
	@AssistedInject
	MatchTextWorker(MatchTextWorkerLogger logger, @Assisted URI resource,
			@Assisted Pattern pattern, @Assisted Charset charset)
			throws MalformedURLException {
		this(logger, resource.toURL(), pattern, charset);
	}

	/**
	 * Sets the file, the comparison text and the character set.
	 * 
	 * @param logger
	 *            the {@link MatchTextWorkerLogger} for logging messages.
	 * 
	 * @param resource
	 *            the {@link URL} of the resource which content is read.
	 * 
	 * @param pattern
	 *            the comparison {@link Pattern}.
	 * 
	 * @param charset
	 *            the {@link Charset} of the text file.
	 */
	@AssistedInject
	MatchTextWorker(MatchTextWorkerLogger logger, @Assisted URL resource,
			@Assisted Pattern pattern, @Assisted Charset charset) {
		this.log = logger;
		this.resource = resource;
		this.pattern = pattern;
		this.charset = new SerializableCharset(charset);
	}

	@Override
	public MatchTextWorker call() throws WorkerException {
		String string = readFile();
		compareText(string);
		return this;
	}

	private void compareText(String string) {
		matches = pattern.matcher(string).find();
		if (matches) {
			log.textWasFound(this);
		} else {
			log.textWasNotFound(this);
		}
	}

	private String readFile() throws WorkerException {
		try {
			Charset encoding = charset.getCharset();
			InputStream input = resource.openStream();
			return IOUtils.toString(input, encoding);
		} catch (IOException e) {
			throw log.readFileError(this, e);
		}
	}

	/**
	 * Returns if the comparison pattern matches or not.
	 * 
	 * @return {@code true} if it's matching, {@code false} if not.
	 */
	public boolean isMatches() {
		return matches;
	}

	/**
	 * Returns the text resource in which the text is compared to.
	 * 
	 * @return the {@link URL}.
	 */
	public URL getResource() {
		return resource;
	}

	/**
	 * Returns the comparison pattern.
	 * 
	 * @return the comparison {@link Pattern}.
	 */
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(resource)
				.append("pattern", pattern).append("character set", charset)
				.toString();
	}
}
