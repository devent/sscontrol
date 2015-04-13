/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.backups.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.backups.archive.ArchiveServiceBackup
import com.anrisoftware.sscontrol.httpd.backups.database.DatabaseBackup
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * Backup service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class ServiceBackup {

    private Object script

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Backups the <i>Yourls</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void backupService(Domain domain, WebService service) {
        if (service.backupTarget != null) {
            File source = serviceDir domain, service
            serviceBackup.backupService domain, service, source
            mysqlDatabaseBackup.backupDatabase domain, service, source
            setupPermissions domain, service
        }
    }

    /**
     * Sets the owner and permissions of the backup archives.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void setupPermissions(Domain domain, WebService service) {
        def user = domain.domainUser
        def dir = backupDir domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: "root",
                ownerGroup: "root",
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rwX,g=rX,o-rwX",
                files: dir,
                recursive: true,
                this, threads)()
    }

    File backupDir(Domain domain, WebService service) {
        serviceBackup.createArchiveFile(domain, service).parentFile
    }

    /**
     * Returns the archive backup.
     */
    abstract ArchiveServiceBackup getServiceBackup()

    /**
     * Returns the <i>MySQL</i> database backup.
     */
    abstract DatabaseBackup getMysqlDatabaseBackup()

    /**
     * Returns the service directory.
     */
    abstract File serviceDir(Domain domain, WebService service)

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
        serviceBackup.setScript script
        mysqlDatabaseBackup.setScript script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
