package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.ubuntu_10_04

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2.AuthFileConfig;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig

/**
 * Auth/file Ubuntu 10.04 configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends AuthFileConfig implements ServiceConfig {

    @Inject
    UbuntuPropertiesProvider authProperties

    @Override
    ContextProperties getAuthProperties() {
        authProperties.get()
    }

    @Override
    String getProfile() {
        PROFILE
    }
}
