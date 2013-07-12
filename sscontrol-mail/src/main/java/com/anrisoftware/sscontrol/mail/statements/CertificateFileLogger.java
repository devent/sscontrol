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
