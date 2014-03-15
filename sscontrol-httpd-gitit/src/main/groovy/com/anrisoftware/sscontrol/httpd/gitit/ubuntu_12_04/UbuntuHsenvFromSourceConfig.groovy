package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.gitit.fromsource.HsenvFromSource

/**
 * Installs and configures <i>Gitit</i> with the help of <i>hsenv</i>
 * from source for <i>Ubuntu</i> 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuHsenvFromSourceConfig extends HsenvFromSource {

    @Inject
    HsenvPropertiesProvider hsenvProperties

    @Override
    ContextProperties getHsenvProperties() {
        hsenvProperties.get()
    }
}
