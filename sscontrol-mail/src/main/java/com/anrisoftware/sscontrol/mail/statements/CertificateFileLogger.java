package com.anrisoftware.sscontrol.mail.statements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link CertificateFile}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CertificateFileLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link CertificateFile}.
	 */
	CertificateFileLogger() {
		super(CertificateFile.class);
	}

	ServiceException errorFileToURL(Exception cause, String file) {
		ServiceException ex = new ServiceException(
				"Could not parse file to URL", cause);
		ex.addContextValue("file", file);
		if (log.isDebugEnabled()) {
			log.debug(ex.getLocalizedMessage());
		} else {
			log.error("Could not parse file '{}' to URL.", file);
		}
		return ex;
	}

	ServiceException destinationNotFound(CertificateFile certificate,
			FileNotFoundException cause, URL source, File destination) {
		ServiceException ex = new ServiceException("Destination not found",
				cause);
		ex.addContextValue("certificates", certificate);
		ex.addContextValue("source", source);
		ex.addContextValue("destination", destination);
		if (log.isDebugEnabled()) {
			log.debug(ex.getLocalizedMessage());
		} else {
			log.error("Destination not found '{}' for '{}'.", destination,
					source);
		}
		return ex;
	}

	ServiceException errorCopyFile(CertificateFile certificate,
			IOException cause, URL source, File destination) {
		ServiceException ex = new ServiceException(
				"Error copy source to destination", cause);
		ex.addContextValue("certificates", certificate);
		ex.addContextValue("source", source);
		ex.addContextValue("destination", destination);
		if (log.isDebugEnabled()) {
			log.debug(ex.getLocalizedMessage());
		} else {
			log.error("Error copy source to destination '{}', source '{}'.",
					destination, source);
		}
		return ex;
	}
}
