package com.anrisoftware.sscontrol.mail.service;

import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Returns extended information about the mail service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class MailServiceScriptInfo extends ServiceScriptInfo {

	/**
	 * Returns the storage type of the mail service for the virtual domains and
	 * users. For example, postfix can store the virtual domains and users in
	 * hash files, in MySQL databases.
	 * 
	 * @return the storage type {@link String} name.
	 */
	public abstract String getStorage();
}
