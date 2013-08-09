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

import static org.apache.commons.io.IOUtils.copy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.resources.ToURL;
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
	 * @see CertificateFileFactory#create(Object, Object, Object)
	 */
	@Inject
	CertificateFile(CertificateFileLogger logger, ToURL tourl,
			@Assisted("file") Object file, @Assisted("keyFile") Object keyFile,
			@Assisted("caFile") Object caFile) throws ServiceException {
		this.log = logger;
		this.file = tourl.convert(file);
		this.keyFile = tourl.convert(keyFile);
		this.caFile = tourl.convert(caFile);
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
