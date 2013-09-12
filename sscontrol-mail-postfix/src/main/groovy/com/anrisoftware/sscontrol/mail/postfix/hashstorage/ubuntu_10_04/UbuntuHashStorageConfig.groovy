package com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux.BaseHashStorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.Ubuntu_10_04_ScriptFactory

/**
 * Hash/Ubuntu 10.04 storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuHashStorageConfig extends BaseHashStorageConfig {

	@Inject
	UbuntuHashStoragePropertiesProvider ubuntuHashStorageProperties

	@Override
	String getProfile() {
		Ubuntu_10_04_ScriptFactory.PROFILE_NAME
	}

	@Override
	ContextProperties getStorageProperties() {
		ubuntuHashStorageProperties.get()
	}
}
