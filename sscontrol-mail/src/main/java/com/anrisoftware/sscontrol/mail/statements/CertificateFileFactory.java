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

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create the location of the certificate, certificate key and CA
 * file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CertificateFileFactory {

	/**
	 * Creates the location of the certificate, certificate key and CA file.
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
	 * @return the {@link CertificateFile}
	 * 
	 * @throws ServiceException
	 *             if one the specified locations could not be parsed in a valid
	 *             URL.
	 */
	CertificateFile create(@Assisted("file") Object file,
			@Assisted("keyFile") Object keyFile,
			@Assisted("caFile") Object caFile) throws ServiceException;
}
