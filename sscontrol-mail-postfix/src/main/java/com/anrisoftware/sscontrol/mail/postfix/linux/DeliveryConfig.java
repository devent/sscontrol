package com.anrisoftware.sscontrol.mail.postfix.linux;

import com.anrisoftware.sscontrol.mail.postfix.script.linux.BasePostfixScript;

/**
 * Delivery configuration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DeliveryConfig {

	/**
	 * Returns the profile name of the storage.
	 * 
	 * @return the profile {@link String} name.
	 */
	String getProfile();

	/**
	 * Returns the delivery name.
	 * 
	 * @return the delivery {@link String} name.
	 */
	String getDeliveryName();

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
	void deployDelivery();

}
