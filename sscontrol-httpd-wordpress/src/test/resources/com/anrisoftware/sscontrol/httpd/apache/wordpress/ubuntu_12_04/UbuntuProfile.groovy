/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuResources


profile "ubuntu_12_04", {
    httpd {
        service "apache"
        install_command UbuntuResources.aptitudeCommand.asFile(tmp)
        chmod_command UbuntuResources.chmodCommand.asFile(tmp)
        chown_command UbuntuResources.chownCommand.asFile(tmp)
        group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
        user_add_command UbuntuResources.useraddCommand.asFile(tmp)
        reconfigure_command UbuntuResources.reconfigureCommand.asFile(tmp)
        zcat_command UbuntuResources.zcatCommand.asFile(tmp)
        tar_command UbuntuResources.tarCommand.asFile(tmp)
        unzip_command UbuntuResources.unzipCommand.asFile(tmp)
        link_command UbuntuResources.lnCommand.asFile(tmp)
        temp_directory UbuntuResources.tmpDir.asFile(tmp)
        php_fcgi_php_conf_directory WordpressResources.phpConfDir.asFile(tmp)
        wordpress_auth_key "auth-key"
        wordpress_secure_auth_key "secure-auth-key"
        wordpress_logged_in_key "logged-in-key"
        wordpress_nonce_key "nonce-key"
        wordpress_auth_salt "auth-salt"
        wordpress_secure_auth_salt "secure-auth-salt"
        wordpress_logged_in_salt "logged-in-salt"
        wordpress_nonce_salt "nonce-salt"
        wordpress_archive WordpressResources.wordpressArchive.resource
        wordpress_archive_hash WordpressResources.wordpressArchiveHash.resource
        wordpress_archive_de_DE WordpressResources.wordpressArchive_de_DE.resource
        wordpress_broken_link_checker WordpressResources.brokenLinkCheckerArchive.resource
        wordpress_hyphenator WordpressResources.hyphenatorArchive.resource
        wordpress_hyper_cache WordpressResources.hypercacheArchive.resource
        wordpress_picochic WordpressResources.picochicArchive.resource
        wordpress_tagebuch WordpressResources.tagebuchArchive.resource
    }
}
