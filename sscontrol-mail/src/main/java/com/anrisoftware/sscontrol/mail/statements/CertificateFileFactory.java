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
