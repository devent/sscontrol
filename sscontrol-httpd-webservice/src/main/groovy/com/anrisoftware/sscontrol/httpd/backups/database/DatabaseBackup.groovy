/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.backups.database

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.joda.time.DateTime.now
import static org.joda.time.format.ISODateTimeFormat.dateHourMinuteSecondMillis
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Backups the database of a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class DatabaseBackup {

    @Inject
    private DatabaseLogger logg

    private Object script

    /**
     * Backups the service database.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param source
     *            the source {@link File} directory.
     */
    void backupDatabase(Domain domain, WebService service, File source) {
        if (!source.exists()) {
            return
        }
        def archiveFile = createDatabaseArchiveFile domain, service
        archiveFile.parentFile.mkdirs()
        execBackupScript service, archiveFile
        logg.databaseBackup service, source, archiveFile
    }

    /**
     * Runs the database backup script.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param archiveFile
     *            the backup archive {@link File} file.
     */
    abstract void execBackupScript(WebService service, File archiveFile)

    /**
     * Returns the database backup archive file. The following placeholders
     * are replaces accordingly;
     *
     * <ul>
     * <li>{@code "domainName"} the name of the domain;</li>
     * <li>{@code "timestamp"} the timestamp;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return the archive {@link File} file.
     *
     * @see #getBackupDatabaseArchive()
     * @see #getTimestamp()
     */
    File createDatabaseArchiveFile(Domain domain, WebService service) {
        def parent = new File(service.backupTarget)
        def st = new ST(backupDatabaseArchive).add("domainName", domain.name).add("timestamp", timestamp)
        new File(parent, st.render())
    }

    /**
     * Returns the timestamp for the archive file.
     */
    String getTimestamp() {
        def date = dateHourMinuteSecondMillis().print now()
        def time = replaceChars(date, ':', '-')
        replaceChars(time, '.', '-')
    }

    /**
     * Returns the backup database archive file template, for
     * example {@code "service_database_<domainName>_<timestamp>.gz"}.
     * The placeholder are replaced with the following values:
     *
     * <ul>
     * <li>{@code "<domainName>"} with the current domain name;</li>
     * <li>{@code "<timestamp>"} with the current time stamp;</li>
     * </ul>
     */
    abstract String getBackupDatabaseArchive()

    /**
     * Returns the timeout to backup the service, for
     * example {@code "PT1H"}.
     */
    abstract Duration getBackupTimeout()

    /**
     * Returns the backup script template resource.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getBackupTemplate()

    /**
     * Returns the service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
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
