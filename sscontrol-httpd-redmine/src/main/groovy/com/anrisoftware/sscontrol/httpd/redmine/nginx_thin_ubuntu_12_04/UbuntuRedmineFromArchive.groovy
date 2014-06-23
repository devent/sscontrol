package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.redmine.fromarchive.RedmineFromArchive

/**
 * Installs and configures <i>Ubuntu 12.04 Redmine</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuRedmineFromArchive extends RedmineFromArchive {

    @Inject
    RedmineFromArchivePropertiesProvider propertiesProvider

    @Override
    ContextProperties getRedmineFromArchiveProperties() {
        propertiesProvider.get()
    }
}
