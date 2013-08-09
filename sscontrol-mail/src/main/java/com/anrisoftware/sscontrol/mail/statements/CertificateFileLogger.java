/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link CertificateFile}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CertificateFileLogger extends AbstractLogger {

	private static final String ERROR_COPY_SOURCE_MESSAGE = "Error copy source to destination '%s', source '%s'.";
	private static final String ERROR_COPY_SOURCE = "Error copy source to destination";
	private static final String DESTINATION_NOT_FOUND_MESSAGE = "Destination not found '%s' for '%s'.";
	private static final String DESTINATION = "destination";
	private static final String SOURCE = "source";
	private static final String CERTIFICATE = "certificate";
	private static final String DESTINATION_NOT_FOUND = "Destination not found";
	private static final String FILE = "file";
	private static final String ERROR_FILE_URL_MESSAGE = "Error parse file '%s' to URL.";
	private static final String ERROR_FILE_URL = "Error parse file to URL";

	/**
	 * Create logger for {@link CertificateFile}.
	 */
	CertificateFileLogger() {
		super(CertificateFile.class);
	}

	ServiceException errorFileToURL(Exception e, String file) {
		return logException(
				new ServiceException(ERROR_FILE_URL, e).add(FILE,
						file), ERROR_FILE_URL_MESSAGE, file);
	}

	ServiceException destinationNotFound(CertificateFile certificate,
			FileNotFoundException e, URL source, File destination) {
		return logException(
				new ServiceException(DESTINATION_NOT_FOUND, e)
						.add(CERTIFICATE, certificate)
						.add(SOURCE, source)
						.add(DESTINATION, destination),
				DESTINATION_NOT_FOUND_MESSAGE, destination, source);
	}

	ServiceException errorCopyFile(CertificateFile certificate,
			IOException cause, URL source, File destination) {
		return logException(
				new ServiceException(ERROR_COPY_SOURCE, cause)
						.add(CERTIFICATE, certificate)
						.add(SOURCE, source)
						.add(DESTINATION, destination),
				ERROR_COPY_SOURCE_MESSAGE, destination, source);
	}
}
