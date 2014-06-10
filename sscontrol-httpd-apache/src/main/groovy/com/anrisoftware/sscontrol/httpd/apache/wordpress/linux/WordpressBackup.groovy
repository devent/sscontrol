/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.linux

import static org.apache.commons.lang3.StringUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.pack.PackFactory

/**
 * Backups the <i>Wordpress</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WordpressBackup {

    @Inject
    private WordpressBackupLogger log

    @Inject
    PackFactory packFactory

    LinuxScript script

    /**
     * Backups the <i>Wordpress</i> service.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     */
    void backup(Domain domain, WordpressService service) {
        backupWordpress domain, service
    }

    /**
     * Backups the <i>Wordpress</i> service files.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link WordpressService} <i>Wordpress</i> service.
     */
    void backupWordpress(Domain domain, WordpressService service) {
        def dir = wordpressDir domain, service
    }

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
