package com.anrisoftware.sscontrol.mail.statements;

import static org.apache.commons.io.IOUtils.copy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.assistedinject.Assisted;

/**
 * The location of the certificate, certificate key and CA file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CertificateFile {

	private final CertificateFileLogger log;

	private final URL file;

	private final URL keyFile;

	private final URL caFile;

	/**
	 * Sets the location of the certificate, certificate key and CA file.
	 * 
	 * @param logger
	 *            the {@link CertificateFileLogger} for logging messages.
	 * 
	 * @param file
	 *            the location of the certificate file.
	 * 
	 * @param keyFile
	 *            the location of the certificate key file.
	 * 
	 * @param caFile
	 *            the location of the certificate CA file.
	 * 
	 * @throws ServiceException
	 *             if one the specified locations could not be parsed in a valid
	 *             URL.
	 */
	@Inject
	CertificateFile(CertificateFileLogger logger,
			@Assisted("file") String file, @Assisted("keyFile") String keyFile,
			@Assisted("caFile") String caFile) throws ServiceException {
		this.log = logger;
		this.file = toURL(file);
		this.keyFile = toURL(keyFile);
		this.caFile = toURL(caFile);
	}

	private URL toURL(String file) throws ServiceException {
		try {
			return new URI(file).toURL();
		} catch (MalformedURLException e) {
			throw log.errorFileToURL(e, file);
		} catch (URISyntaxException e) {
			throw log.errorFileToURL(e, file);
		}
	}

	/**
	 * Returns the location of the certificate file.
	 * 
	 * @return the {@link URL} to the certificate file.
	 */
	public URL getCaFileURL() {
		return caFile;
	}

	/**
	 * Returns the location of the certificate key file.
	 * 
	 * @return the {@link URL} to the certificate key file.
	 */
	public URL getFileURL() {
		return file;
	}

	/**
	 * Returns the location of the certificate CA file.
	 * 
	 * @return the {@link URL} to the certificate CA file.
	 */
	public URL getKeyFileURL() {
		return keyFile;
	}

	/**
	 * Copies the certificate file to the specified destination.
	 * 
	 * @param destination
	 *            the destination {@link File}.
	 * 
	 * @throws ServiceException
	 *             if the destination file was not found or the certificate file
	 *             could not be copied.
	 */
	public void copyFile(File destination) throws ServiceException {
		copyFile(file, destination);
	}

	/**
	 * Copies the certificate key file to the specified destination.
	 * 
	 * @param destination
	 *            the destination {@link File}.
	 * 
	 * @throws ServiceException
	 *             if the destination file was not found or the certificate key
	 *             file could not be copied.
	 */
	public void copyKeyFile(File destination) throws ServiceException {
		copyFile(keyFile, destination);
	}

	/**
	 * Copies the certificate CA file to the specified destination.
	 * 
	 * @param destination
	 *            the destination {@link File}.
	 * 
	 * @throws ServiceException
	 *             if the destination file was not found or the certificate CA
	 *             file could not be copied.
	 */
	public void copyCaFile(File destination) throws ServiceException {
		copyFile(caFile, destination);
	}

	private void copyFile(URL source, File destination) throws ServiceException {
		try {
			copy(source.openStream(), new FileOutputStream(destination));
		} catch (FileNotFoundException e) {
			throw log.destinationNotFound(this, e, source, destination);
		} catch (IOException e) {
			throw log.errorCopyFile(this, e, source, destination);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("certificate file", file)
				.append("certificate key file", keyFile)
				.append("certificate CA file", caFile).toString();
	}
}
