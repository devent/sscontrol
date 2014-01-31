package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Returns <i>Gitit</i> properties for <i>Ubuntu 12.04</i> from
 * {@code /gitit_ubuntu_12_04.properties}
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class GititPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL URL = GititPropertiesProvider.class
            .getResource("/gitit_ubuntu_12_04.properties");

    GititPropertiesProvider() {
        super(GititPropertiesProvider.class, URL);
    }
}
