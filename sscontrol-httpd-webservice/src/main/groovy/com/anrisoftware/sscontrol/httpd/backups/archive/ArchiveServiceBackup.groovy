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
package com.anrisoftware.sscontrol.httpd.backups.archive

import static org.apache.commons.lang3.StringUtils.replaceChars
import static org.joda.time.DateTime.now
import static org.joda.time.format.ISODateTimeFormat.dateHourMinuteSecondMillis
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.pack.PackFactory

/**
 * Backups the web service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class ArchiveServiceBackup {

    @Inject
    private ArchiveServiceLogger logg

    @Inject
    PackFactory packFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    Object parent

    /**
     * Backups the service files.
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
    void backupService(Domain domain, WebService service, File source) {
        if (!source.exists()) {
            return
        }
        def output = createArchiveFile domain, service
        output.parentFile.mkdirs()
        packFactory.create(
                log: log,
                files: source,
                commands: unpackCommands,
                output: output,
                timeout: backupTimeout,
                parent, threads)()
        logg.serviceBackup service, source, output
    }

    /**
     * Returns the backup archive file. The following placeholders
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
     */
    File createArchiveFile(Domain domain, WebService service) {
        def parent = new File(service.backupTarget)
        def st = new ST(backupArchive).add("domainName", domain.name).add("timestamp", timestamp)
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
     * Backup archive file template, for
     * example {@code "service_<domainName>_<timestamp>.tar.gz"}.
     */
    abstract String getBackupArchive()

    /**
     * The timeout to backup the service, for
     * example {@code "PT1H"}.
     */
    abstract Duration getBackupTimeout()

    void setScript(Object parent) {
        this.parent = parent
    }

    def propertyMissing(String name) {
        parent.getProperty name
    }

    def methodMissing(String name, def args) {
        parent.invokeMethod name, args
    }
}
