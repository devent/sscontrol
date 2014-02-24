package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Returns <i>hsenv</i> <i>Gitit</i> properties for <i>Ubuntu 12.04</i> from
 * {@code /gitit_hsenvfromsource_ubuntu_12_04.properties}
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class HsenvPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL URL = HsenvPropertiesProvider.class
            .getResource("/gitit_hsenvfromsource_ubuntu_12_04.properties");

    HsenvPropertiesProvider() {
        super(HsenvPropertiesProvider.class, URL);
    }
}
