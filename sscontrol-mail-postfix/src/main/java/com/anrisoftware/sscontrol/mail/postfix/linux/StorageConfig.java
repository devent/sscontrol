package com.anrisoftware.sscontrol.mail.postfix.linux;

import com.anrisoftware.sscontrol.mail.postfix.script.linux.BasePostfixScript;

/**
 * Storage configuration for virtual domains and users.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface StorageConfig {

	/**
	 * Returns the profile name of the storage.
	 * 
	 * @return the profile {@link String} name.
	 */
	String getProfile();

	/**
	 * Returns the storage name.
	 * 
	 * @return the storage {@link String} name.
	 */
	String getStorageName();

	/**
	 * Sets the parent script with the properties.
	 * 
	 * @param script
	 *            the {@link BasePostfixScript}.
	 */
	void setScript(BasePostfixScript script);

	/**
	 * Returns the parent script with the properties.
	 * 
	 * @return the {@link BasePostfixScript}.
	 */
	BasePostfixScript getScript();

	/**
	 * Creates the configuration.
	 */
	void deployStorage();

}
