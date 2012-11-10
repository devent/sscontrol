/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-text.
 *
 * sscontrol-workers-text is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-text is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-text. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.match.worker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import com.anrisoftware.sscontrol.workers.api.WorkerFactory;

/**
 * Factory to create a new worker that compares the content of a text file or
 * resource to a given pattern.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface MatchTextWorkerFactory extends WorkerFactory {

	/**
	 * Creates a new worker that compares the content of a text file to a given
	 * pattern.
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
	 * @return the {@link MatchTextWorker}.
	 * 
	 * @throws MalformedURLException
	 *             if the specified file can not be converted to a URL.
	 */
	MatchTextWorker create(File file, Pattern pattern, Charset charset)
			throws MalformedURLException;

	/**
	 * Creates a new worker that compares the content of a text resource to a
	 * given pattern.
	 * 
	 * @param resource
	 *            the {@link URI} of the resource which content is read.
	 * 
	 * @param pattern
	 *            the comparison {@link Pattern}.
	 * 
	 * @param charset
	 *            the {@link Charset} of the text file.
	 * 
	 * @return the {@link MatchTextWorker}.
	 * 
	 * @throws MalformedURLException
	 *             if the specified resource URI can not be converted to a URL.
	 */
	MatchTextWorker create(URI resource, Pattern pattern, Charset charset);

	/**
	 * Creates a new worker that compares the content of a text resource to a
	 * given pattern.
	 * 
	 * @param resource
	 *            the {@link URL} of the resource which content is read.
	 * 
	 * @param pattern
	 *            the comparison {@link Pattern}.
	 * 
	 * @param charset
	 *            the {@link Charset} of the text file.
	 * 
	 * @return the {@link MatchTextWorker}.
	 */
	MatchTextWorker create(URL resource, Pattern pattern, Charset charset);
}
